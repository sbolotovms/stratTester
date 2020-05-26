package com.cepgamer.strattester.execution

import com.cepgamer.strattester.data.DataDownloadManager
import com.cepgamer.strattester.parser.YahooJSONParser
import com.cepgamer.strattester.runner.SavedDataTraderRunner
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.trader.BaseTrader
import com.cepgamer.strattester.util.StratLogger
import java.io.File
import java.math.BigDecimal
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class TraderTestingExecutor(
    symbol: String,
    startDate: YearMonth,
    endDate: YearMonth,
    val traders: List<BaseTrader>
): BaseExecutor(symbol, startDate, endDate) {

    fun performRun(
        data: List<Pair<Stock, PriceCandle>>,
        fileSuffix: String
    ) {
        val runner = SavedDataTraderRunner(
            traders, false, listOf(data)
        )
        StratLogger.i(
            """
            Trader count: ${traders.size}
            Total timestamp count: ${data.size}
        """
        )
        runner.run()
        val firstOpen = data.first().second.open
        val lastClose = data.last().second.close

        val priceDiffPercent = lastClose / firstOpen * BigDecimal(100)
        StratLogger.i(
            """
            Opened at: $firstOpen
            Closed at: $lastClose
            Gain/loss: $priceDiffPercent %
            
            Best strat gain: ${traders.maxBy { it.money }?.money}
            """
        )

        val res = tradersReport(
            traders.filter { it.transactions.isNotEmpty() },
            successfulCriteria = moneyAvailable.max(moneyAvailable * (data.last().second.close / data.first().second.close))
        )

        val formatter = DateTimeFormatter.ofPattern("yy-MMM")
        val prefix = "${symbol}/${startDate.format(formatter)}_${endDate.format(formatter)}"
        val writeFile = { result: String ->
            { it: File ->
                it.apply {
                    parentFile.mkdirs()
                    createNewFile()
                    writeText(result)
                }
            }
        }
        File("$prefix/res_$fileSuffix.txt").let(writeFile(res))
    }

    fun runDailyData(
        rawData: List<PriceCandle>
    ) {
        val dailyData = PriceCandle.toDaily(rawData).map {
            security to it
        }

        StratLogger.i("Running daily data")
        performRun(
            dailyData,
            "daily"
        )
    }

    fun runHourlyData(
        rawData: List<PriceCandle>
    ) {
        val data = rawData.map {
            security to it
        }
        StratLogger.i("Running hourly data")
        performRun(
            data,
            "hourly"
        )
    }

    override fun execute() {
        val downloader = DataDownloadManager(symbol, startDate, endDate)
        val rawData = downloader.yahooJsons.map { YahooJSONParser(it).parse() }.reduce { acc, list -> acc + list }

        runDailyData(rawData)
        runHourlyData(rawData)
    }
}

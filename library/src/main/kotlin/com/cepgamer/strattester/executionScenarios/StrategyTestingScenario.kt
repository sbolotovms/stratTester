package com.cepgamer.strattester.executionScenarios

import com.cepgamer.strattester.data.DataDownloadManager
import com.cepgamer.strattester.generator.StrategyListGenerator
import com.cepgamer.strattester.generator.TraderGenerator
import com.cepgamer.strattester.parser.YahooJSONParser
import com.cepgamer.strattester.runner.SavedDataTraderRunner
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.trader.BaseTrader
import com.cepgamer.strattester.util.StratLogger
import java.io.File
import java.math.BigDecimal
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class StrategyTestingScenario(
    val symbol: String,
    val startDate: YearMonth,
    val endDate: YearMonth
) {
    val security = Stock(symbol)

    fun moneyAvailable(): Dollar = Dollar(10000)

    fun tradersReport(
        traders: List<BaseTrader>,
        reportAll: Boolean = false,
        successfulCriteria: BigDecimal = moneyAvailable()
    ): String {
        val reportTop = 75
        return """${if (reportAll) traders.toString() else ""}
----------------------------------------------------------------
            Total Traders: ${traders.size}
            Any successful traders: ${traders.find { it.money > successfulCriteria } != null}
            Top $reportTop Successful traders: ${traders.filter { it.money > successfulCriteria }
            .sortedBy { it.money }.takeLast(reportTop)}
----------------------------------------------------------------
        """
    }

    fun performRun(
        data: List<Pair<Stock, PriceCandle>>,
        haveCustom: Boolean = false,
        haveMetricCutoffs: Boolean = false,
        havePLCutoffs: Boolean = true,
        haveInverse: Boolean = true,
        fileSuffix: String
    ) {
        val strats = StrategyListGenerator(
            security,
            haveCustom = haveCustom,
            haveMetricCutoffs = haveMetricCutoffs,
            haveInverse = haveInverse
        ).generate()
        val traders = TraderGenerator(strats, havePLCutoffs = havePLCutoffs).generate().map { it() }
        val runner = SavedDataTraderRunner(
            traders, listOf(data)
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
            successfulCriteria = moneyAvailable().max(moneyAvailable() * (data.last().second.close / data.first().second.close))
        )

        val formatter = DateTimeFormatter.ofPattern("MMM")
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

    fun runStrategyTests(
        haveCustom: Boolean = true,
        haveMetricCutoffs: Boolean = true,
        havePLCutoffs: Boolean = true,
        haveInverse: Boolean = true
    ) {
        val downloader = DataDownloadManager(symbol, startDate, endDate)
        val rawData = downloader.yahooJsons.map { YahooJSONParser(it).parse() }.reduce { acc, list -> acc + list }
        val data = rawData.map {
            security to it
        }
        val dailyData = PriceCandle.toDaily(data.map { it.second }).map {
            security to it
        }

        println("Running daily data")
        performRun(
            dailyData,
            haveCustom,
            haveMetricCutoffs,
            havePLCutoffs,
            haveInverse,
            "daily"
        )

        println("Running hourly data")
        performRun(
            data,
            haveCustom,
            haveMetricCutoffs,
            havePLCutoffs,
            haveInverse,
            "hourly"
        )
        return
        val dailyStrats = StrategyListGenerator(
            security,
            haveCustom = haveCustom,
            haveMetricCutoffs = haveMetricCutoffs,
            haveInverse = haveInverse
        ).generate()

        val tradersByDay = TraderGenerator(dailyStrats, havePLCutoffs = havePLCutoffs).generate().map { it() }

        val dailyRunner = SavedDataTraderRunner(
            tradersByDay, listOf(dailyData)
        )


        dailyRunner.run()

        val dailyRes =
            tradersReport(
                tradersByDay.filter { it.transactions.isNotEmpty() },
                successfulCriteria = moneyAvailable().max(moneyAvailable() * (rawData.last().close / rawData.first().close))
            )
    }
}

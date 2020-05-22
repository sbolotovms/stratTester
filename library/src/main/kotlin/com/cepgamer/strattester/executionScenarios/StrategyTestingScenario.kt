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
        return """${if (reportAll) traders.toString() else ""}
----------------------------------------------------------------
            Total Traders: ${traders.size}
            Any successful traders: ${traders.find { it.money > successfulCriteria } != null}
            Top 20 Successful traders: ${traders.filter { it.money > successfulCriteria }
            .sortedBy { it.money }.takeLast(20)}
----------------------------------------------------------------
        """
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

        val dailyStrats = StrategyListGenerator(security,
            haveCustom = haveCustom,
            haveMetricCutoffs = haveMetricCutoffs,
            haveInverse = haveInverse).generate()
        val strats = StrategyListGenerator(security,
            haveCustom = haveCustom,
            haveMetricCutoffs = haveMetricCutoffs,
            haveInverse = haveInverse).generate()

        val tradersByDay = TraderGenerator(dailyStrats, havePLCutoffs = havePLCutoffs).generate().map { it() }
        val tradersByHour = TraderGenerator(strats, havePLCutoffs = havePLCutoffs).generate().map { it() }

        val dailyRunner = SavedDataTraderRunner(
            tradersByDay, listOf(dailyData)
        )
        val runner = SavedDataTraderRunner(
            tradersByHour, listOf(data)
        )

        StratLogger.i("""
            Trader count: ${tradersByHour.size}
            Total timestamp count: ${data.size}
            Daily timestamp count: ${dailyData.size}
        """)

        dailyRunner.run()
        runner.run()

        val firstOpen = data.first().second.open
        val lastClose = data.last().second.close
        val priceDiffPercent = lastClose / firstOpen * BigDecimal(100)
        StratLogger.i(
            """
            Opened at: $firstOpen
            Closed at: $lastClose
            Gain/loss: $priceDiffPercent %
            
            Best hourly strat gain: ${tradersByHour.maxBy { it.money }?.money}
            Best daily strat gain: ${tradersByDay.maxBy { it.money }?.money}
            """
        )

        val dailyRes =
            tradersReport(
                tradersByDay.filter { it.transactions.isNotEmpty() },
                successfulCriteria = moneyAvailable().max(moneyAvailable() * (rawData.last().close / rawData.first().close))
            )
        val res = tradersReport(tradersByHour.filter { it.transactions.isNotEmpty() },
            successfulCriteria = moneyAvailable().max(moneyAvailable() * (rawData.last().close / rawData.first().close))
        )

        val prefix = "${symbol}/${testingMonths.joinToString("_")}"
        val writeFile = { result: String ->
            { it: File ->
                it.apply {
                    parentFile.mkdirs()
                    createNewFile()
                    writeText(result)
                }
            }
        }
        File("$prefix/dailyRes.txt").let(writeFile(dailyRes))

        File("$prefix/res.txt").let(writeFile(res))
    }
}

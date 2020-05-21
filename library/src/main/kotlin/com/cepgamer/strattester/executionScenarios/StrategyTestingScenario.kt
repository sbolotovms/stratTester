package com.cepgamer.strattester.executionScenarios

import com.cepgamer.strattester.StrategyListGenerator
import com.cepgamer.strattester.data.YahooWebDownloader
import com.cepgamer.strattester.parser.YahooJSONParser
import com.cepgamer.strattester.runner.SavedDataTraderRunner
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.trader.BaseTrader
import com.cepgamer.strattester.trader.StrategyTrader
import java.io.File
import java.math.BigDecimal

class StrategyTestingScenario(val testingMonths: Set<String>, val symbol: String) {
    companion object {
        const val feb1 = YahooWebDownloader.feb1
        const val mar1 = YahooWebDownloader.mar1
        const val apr1 = YahooWebDownloader.apr1
        const val may1 = YahooWebDownloader.may1
        val availableMonths = setOf("feb", "mar", "apr")//, "may")

        private val monthsMapping = mapOf(
            "feb" to (feb1 to mar1),
            "mar" to (mar1 to apr1),
            "apr" to (apr1 to may1)
        )
    }

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

    val yahooJsons: List<String>
        get() = monthsMapping.entries.filter { testingMonths.contains(it.key) }.map {
            YahooWebDownloader.getYahooHourlyData(symbol, it.value.first, it.value.second, it.key)
        }

    fun runStrategyTests(
        haveCustom: Boolean = true,
        haveMetricCutoffs: Boolean = true,
        havePLCutoffs: Boolean = true,
        haveInverse: Boolean = true
    ) {
        val rawData = yahooJsons.map { YahooJSONParser(it).parse() }.reduce { acc, list -> acc + list }
        val data = rawData.map {
            security as Stock to it
        }
        val dailyData = PriceCandle.toDaily(data.map { it.second }).map {
            security to it
        }

        val dailyStrats = StrategyListGenerator(security).generate(
            haveCustom = haveCustom,
            haveMetricCutoffs = haveMetricCutoffs,
            havePLCutoffs = havePLCutoffs,
            haveInverse = haveInverse
        )
        val strats = StrategyListGenerator(security).generate(
            haveCustom = haveCustom,
            haveMetricCutoffs = haveMetricCutoffs,
            havePLCutoffs = havePLCutoffs,
            haveInverse = haveInverse
        )

        val tradersByDay = dailyStrats.map { StrategyTrader(it, moneyAvailable()) }
        val tradersByHour = strats.map { StrategyTrader(it, moneyAvailable()) }

        val dailyRunner = SavedDataTraderRunner(
            dailyStrats, listOf(dailyData)
        )
        val runner = SavedDataTraderRunner(
            strats, listOf(data)
        )

        dailyRunner.run()
        runner.run()

        val firstOpen = data.first().second.open
        val lastClose = data.last().second.close
        val priceDiffPercent = lastClose / firstOpen * BigDecimal(100)
        println(
            """
            Opened at: $firstOpen
            Closed at: $lastClose
            Gain/loss: $priceDiffPercent
            
            Best hourly strat gain: ${strats.maxBy { it.money }?.money}
            Best daily strat gain: ${dailyStrats.maxBy { it.money }?.money}
            """
        )

        val dailyRes =
            tradersReport(
                dailyStrats.filter { it.transactions.isNotEmpty() },
                successfulCriteria = moneyAvailable().max(moneyAvailable() * (rawData.last().close / rawData.first().close))
            )
        val res = tradersReport(strats.filter { it.transactions.isNotEmpty() },
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

        val weak = strats.filter { it.money <= BigDecimal(5_000) }
    }
}

package com.cepgamer.strattester.executionScenarios

import com.cepgamer.strattester.StrategyListGenerator
import com.cepgamer.strattester.data.YahooWebDownloader
import com.cepgamer.strattester.parser.YahooJSONParser
import com.cepgamer.strattester.runner.SavedDataStrategyRunner
import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.strategy.BaseStrategy
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

    fun moneyAvailable(): Dollar = Dollar(BigDecimal(10000))

    fun stratsReport(
        strats: List<BaseStrategy>,
        reportAll: Boolean = false,
        successfulCriteria: BigDecimal = moneyAvailable().quantity
    ): String {
        return """${if (reportAll) strats.toString() else ""}
----------------------------------------------------------------
            Total strats: ${strats.size}
            Any successful strats: ${strats.find { it.moneyAvailable.quantity > successfulCriteria } != null}
            Top 20 Successful strats: ${strats.filter { it.moneyAvailable.quantity > successfulCriteria }
            .sortedBy { it.moneyAvailable.quantity }.takeLast(20)}
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
            security as BaseSecurity to it
        }
        val dailyData = PriceCandle.toDaily(data.map { it.second }).map {
            security to it
        }

        val dailyStrats = StrategyListGenerator(security).generate(
            haveCustom = true,
            haveMetricCutoffs = true,
            havePLCutoffs = true,
            haveInverse = true
        )
        val strats = StrategyListGenerator(security).generate(
            haveCustom = true,
            haveMetricCutoffs = true,
            havePLCutoffs = true,
            haveInverse = true
        )

        val dailyRunner = SavedDataStrategyRunner(
            dailyStrats, listOf(dailyData)
        )
        val runner = SavedDataStrategyRunner(
            strats, listOf(data)
        )

        dailyRunner.run()
        runner.run()

        val maxPossible = data.fold(data.first().second.close to BigDecimal(0).setScale(5), { acc, pair ->
            return@fold if (acc.first > pair.second.close) {
                pair.second.close to acc.second + (acc.first - pair.second.close)
            } else pair.second.close to acc.second
        })
        println(maxPossible)

        val dailyRes =
            stratsReport(
                dailyStrats.filter { it.transactions.isNotEmpty() }.sortedBy { it.moneyAvailable.quantity },
                successfulCriteria = moneyAvailable().quantity.max(moneyAvailable().quantity * (rawData.last().close / rawData.first().close))
            )
        val res = stratsReport(strats.filter { it.transactions.isNotEmpty() }.sortedBy { it.moneyAvailable.quantity },
            successfulCriteria = moneyAvailable().quantity.max(moneyAvailable().quantity * (rawData.last().close / rawData.first().close))
        )

        val prefix = "${symbol}/${testingMonths.joinToString("_")}"
        val writeFile = { it: File ->
            it.apply {
                parentFile.mkdirs()
                createNewFile()
                writeText(dailyRes)
            }
        }
        File("$prefix/dailyRes.txt").let(writeFile)

        File("$prefix/res.txt").let(writeFile)

        val weak = strats.filter { it.moneyAvailable.quantity <= BigDecimal(5_000) }
    }
}

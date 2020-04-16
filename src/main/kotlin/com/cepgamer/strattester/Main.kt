package com.cepgamer.strattester

import com.cepgamer.strattester.data.YahooWebDownloader
import com.cepgamer.strattester.metric.InverseMetric
import com.cepgamer.strattester.metric.SimpleGrowthMetric
import com.cepgamer.strattester.metric.SwapSignalMetric
import com.cepgamer.strattester.metric.VolumeAmplifiedGrowth
import com.cepgamer.strattester.parser.YahooJSONParser
import com.cepgamer.strattester.runner.SavedDataStrategyRunner
import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.strategy.BaseStrategy
import com.cepgamer.strattester.strategy.BlankStrategy
import com.cepgamer.strattester.strategy.BuyStrategy
import com.cepgamer.strattester.strategy.MetricCutoffStrategy
import java.math.BigDecimal

object Main {
    val symbol = "SPY"
    val security = Stock(symbol)

    fun moneyAvailable(): Dollar = Dollar(BigDecimal(10000))

    val strats: List<BaseStrategy>
        get() {
            val metricCutoffs =
                MetricCutoffStrategy.generateNStrategies(SimpleGrowthMetric(), security, moneyAvailable(), 10) +
                        MetricCutoffStrategy.generateNStrategies(
                            VolumeAmplifiedGrowth(10),
                            security,
                            moneyAvailable(),
                            10
                        )
            val custom = listOf(
                BlankStrategy(security, moneyAvailable()),
                MetricCutoffStrategy(SimpleGrowthMetric(), security, moneyAvailable(), BigDecimal(0)),
                MetricCutoffStrategy(SwapSignalMetric(SimpleGrowthMetric()), security, moneyAvailable(), BigDecimal(0)),
                MetricCutoffStrategy(InverseMetric(SimpleGrowthMetric()), security, moneyAvailable(), BigDecimal(0)),
                BuyStrategy(security, moneyAvailable())
            )
            val final = metricCutoffs + custom
            return final
        }

    @JvmStatic
    fun main(args: Array<String>) {
        val json = YahooWebDownloader.getYahooHourlyData(
            symbol,
            YahooWebDownloader.feb1,
            YahooWebDownloader.mar1,
            "mar"
        )
        val rawData = YahooJSONParser(json).parse()
        val data = rawData.map {
            security as BaseSecurity to it
        }

        val strats = strats
        val runner = SavedDataStrategyRunner(strats, listOf(data))

        runner.run()

        println(strats)
    }
}
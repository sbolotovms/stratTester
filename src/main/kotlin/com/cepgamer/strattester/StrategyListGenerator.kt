package com.cepgamer.strattester

import com.cepgamer.strattester.metric.*
import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.strategy.*
import java.math.BigDecimal

class StrategyListGenerator(val security: BaseSecurity) {
    fun moneyAvailable(): Dollar {
        return Dollar(10_000)
    }

    private fun generateMetricStrategies(metric: () -> BaseMetric): List<() -> BaseStrategy> {
        val list =
            MetricCutoffStrategy.generateNStrategies(
                metric(),
                security,
                moneyAvailable(),
                10
            )
        return list
    }

    private fun generateInverseMetricStrategies(metric: () -> BaseMetric): List<() -> BaseStrategy> {
        return generateMetricStrategies { InverseMetric(metric()) }
    }

    private fun generateSwappedSignalMetricStrategies(metric: () -> BaseMetric): List<() -> BaseStrategy> {
        return generateMetricStrategies { SwapSignalMetric(metric()) }
    }

    val buyStrategy = BuyStrategy(security, moneyAvailable())

    private fun generateInverseStrategies(strats: List<() -> BaseStrategy>): List<() -> BaseStrategy> {
        return strats.map { { InverseStrategy(it(), security, moneyAvailable()) } }
    }

    private fun generateAllMetricStrategies(metric: () -> BaseMetric): List<BaseStrategy> {
        val allStrats = generateMetricStrategies(metric) +
                generateInverseMetricStrategies(metric) +
                generateSwappedSignalMetricStrategies(metric)
        val alllStrats = allStrats + generateInverseStrategies(allStrats)
        return alllStrats.map { it() }
    }

    fun generate(): List<BaseStrategy> {
        val custom = listOf(
            BlankStrategy(security, moneyAvailable()),
            MetricCutoffStrategy(SimpleGrowthMetric(), security, moneyAvailable(), BigDecimal(0)),
            MetricCutoffStrategy(
                SwapSignalMetric(SimpleGrowthMetric()),
                security,
                moneyAvailable(),
                BigDecimal(0)
            ),
            MetricCutoffStrategy(
                InverseMetric(SimpleGrowthMetric()),
                security,
                moneyAvailable(),
                BigDecimal(0)
            ),
            BuyStrategy(security, moneyAvailable())
        )
        val final = generateAllMetricStrategies { SimpleGrowthMetric() } +
                generateAllMetricStrategies { VolumeAmplifiedGrowth(10) } +
                generateAllMetricStrategies { VolumeAmplifiedGrowth(20) } +
                generateAllMetricStrategies { VolumeAmplifiedGrowth(30) }
        return final
    }
}
package com.cepgamer.strattester.generator

import com.cepgamer.strattester.metric.*
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.strategy.*
import java.math.BigDecimal

class StrategyListGenerator(val security: Stock) {
    fun moneyAvailable(): Dollar {
        return Dollar(10_000)
    }

    private fun generateMetricStrategies(metric: () -> BaseMetric): List<() -> BaseStrategy> {
        val list =
            MetricCutoffStrategy.generateNbyMStrategies(
                metric(),
                security,
                moneyAvailable(),
                10,
                10
            )
        return list
    }

    private fun generateInverseMetricStrategies(
        metric: () -> BaseMetric,
        strats: (() -> BaseMetric) -> List<() -> BaseStrategy>
    ): List<() -> BaseStrategy> {
        return strats { InverseMetric(metric()) }
    }

    private fun generateSwappedSignalMetricStrategies(
        metric: () -> BaseMetric,
        strats: (() -> BaseMetric) -> List<() -> BaseStrategy>
    ): List<() -> BaseStrategy> {
        return strats { SwapSignalMetric(metric()) }
    }

    private fun generateInverseStrategies(strats: List<() -> BaseStrategy>): List<() -> BaseStrategy> {
        return strats.map { { InverseStrategy(it(), security) } }
    }

    private fun generateAllStrategies(
        metric: () -> BaseMetric,
        strats: (() -> BaseMetric) -> List<() -> BaseStrategy>,
        haveInverse: Boolean
    ): List<BaseStrategy> {
        val allStrats = strats(metric) +
                generateInverseMetricStrategies(metric, strats) +
                generateSwappedSignalMetricStrategies(metric, strats)
        val alllStrats = allStrats +
                if (haveInverse) generateInverseStrategies(allStrats)
                else {
                    emptyList()
                }
        return alllStrats.map { it() }
    }

    val buyStrategy = BuyStrategy(security)

    fun generate(
        haveCustom: Boolean = false,
        haveMetricCutoffs: Boolean = false,
        havePLCutoffs: Boolean = false,
        haveInverse: Boolean = true
    ): List<BaseStrategy> {
        val custom = listOf(
            BlankStrategy(security, moneyAvailable()),
            MetricCutoffStrategy(SimpleGrowthMetric(), security, BigDecimal(0)),
            MetricCutoffStrategy(
                SwapSignalMetric(SimpleGrowthMetric()),
                security,
                BigDecimal(0)
            ),
            MetricCutoffStrategy(
                InverseMetric(SimpleGrowthMetric()),
                security,
                BigDecimal(0)
            ),
            BuyStrategy(security)
        )
        val metricCutoffs =
            generateAllStrategies({ SimpleGrowthMetric() }, this::generateMetricStrategies, haveInverse) +
                    generateAllStrategies({ VolumeAmplifiedGrowth(10) }, this::generateMetricStrategies, haveInverse) +
                    generateAllStrategies({ VolumeAmplifiedGrowth(20) }, this::generateMetricStrategies, haveInverse) +
                    generateAllStrategies({ VolumeAmplifiedGrowth(30) }, this::generateMetricStrategies, haveInverse)
        val plCutoffs = generateAllStrategies({ SimpleGrowthMetric() }, this::generatePLCutoffStrategies, haveInverse) +
                generateAllStrategies({ VolumeAmplifiedGrowth(10) }, this::generatePLCutoffStrategies, haveInverse) +
                generateAllStrategies({ VolumeAmplifiedGrowth(20) }, this::generatePLCutoffStrategies, haveInverse) +
                generateAllStrategies({ VolumeAmplifiedGrowth(30) }, this::generatePLCutoffStrategies, haveInverse)

        val final = if (haveCustom) custom else {
            emptyList()
        } + if (haveMetricCutoffs) metricCutoffs else {
            emptyList()
        } + if (havePLCutoffs) plCutoffs else {
            emptyList()
        }
        return final
    }
}
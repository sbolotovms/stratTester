package com.cepgamer.strattester.generator

import com.cepgamer.strattester.metric.*
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.strategy.*
import java.math.BigDecimal

class StrategyListGenerator(
    val security: Stock,
    val haveCustom: Boolean = false,
    val haveMetricCutoffs: Boolean = false,
    val haveInverse: Boolean = true
) : Generator<BaseStrategy> {

    private fun generateMetricStrategies(metric: () -> BaseMetric): List<() -> BaseStrategy> {
        val list =
            MetricCutoffStrategy.generateNbyMStrategies(
                metric(),
                security,
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
    ): List<() -> BaseStrategy> {
        val allStrats = strats(metric) +
                generateInverseMetricStrategies(metric, strats) +
                generateSwappedSignalMetricStrategies(metric, strats)
        val alllStrats = allStrats +
                if (haveInverse) generateInverseStrategies(allStrats)
                else {
                    emptyList()
                }
        return alllStrats
    }

    override fun generate(): List<() -> BaseStrategy> {
        val custom = listOf(
            BlankStrategy(security),
            ConstantStrategy(security, BaseStrategy.Action.BUY)
        ).map { { it } }
        val metricCutoffs =
            generateAllStrategies({ SimpleGrowthMetric() }, this::generateMetricStrategies, haveInverse) +
                    generateAllStrategies({ VolumeAmplifiedGrowth(10) }, this::generateMetricStrategies, haveInverse) +
                    generateAllStrategies({ VolumeAmplifiedGrowth(20) }, this::generateMetricStrategies, haveInverse) +
                    generateAllStrategies({ VolumeAmplifiedGrowth(30) }, this::generateMetricStrategies, haveInverse)

        val final = if (haveCustom) custom else {
            emptyList()
        } + if (haveMetricCutoffs) metricCutoffs else {
            emptyList()
        }
        return final
    }
}
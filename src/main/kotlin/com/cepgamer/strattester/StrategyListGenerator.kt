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
            MetricCutoffStrategy.generateNbyMStrategies(
                metric(),
                security,
                moneyAvailable(),
                10,
                10
            )
        return list
    }

    private fun generatePLCutoffStrategies(metric: () -> BaseMetric): List<() -> BaseStrategy> {
        val list =
            ProfitLossLockStrategy.generateNbyMStrategies(
                metric(),
                security,
                moneyAvailable(),
                listOf(1, 2, 3, 4, 5),
                listOf(1, 2, 3, 4, 5),
                listOf(5, 10, 25, 100, 200),
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
        return strats.map { { InverseStrategy(it(), security, moneyAvailable()) } }
    }

    private fun generateAllStrategies(
        metric: () -> BaseMetric,
        strats: (() -> BaseMetric) -> List<() -> BaseStrategy>
    ): List<BaseStrategy> {
        val allStrats = strats(metric) +
                generateInverseMetricStrategies(metric, strats) +
                generateSwappedSignalMetricStrategies(metric, strats)
        val alllStrats = allStrats + generateInverseStrategies(allStrats)
        return alllStrats.map { it() }
    }

    val buyStrategy = BuyStrategy(security, moneyAvailable())

    fun generate(
        haveCustom: Boolean = false,
        haveMetricCutoffs: Boolean = false,
        havePLCutoffs: Boolean = false
    ): List<BaseStrategy> {
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
        val metricCutoffs = generateAllStrategies({ SimpleGrowthMetric() }, this::generateMetricStrategies) +
                generateAllStrategies({ VolumeAmplifiedGrowth(10) }, this::generateMetricStrategies) +
                generateAllStrategies({ VolumeAmplifiedGrowth(20) }, this::generateMetricStrategies) +
                generateAllStrategies({ VolumeAmplifiedGrowth(30) }, this::generateMetricStrategies)
        val plCutoffs = generateAllStrategies({ SimpleGrowthMetric() }, this::generatePLCutoffStrategies) +
                generateAllStrategies({ VolumeAmplifiedGrowth(10) }, this::generatePLCutoffStrategies) +
                generateAllStrategies({ VolumeAmplifiedGrowth(20) }, this::generatePLCutoffStrategies) +
                generateAllStrategies({ VolumeAmplifiedGrowth(30) }, this::generatePLCutoffStrategies)

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
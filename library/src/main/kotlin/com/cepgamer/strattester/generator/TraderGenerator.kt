package com.cepgamer.strattester.generator

import com.cepgamer.strattester.metric.BaseMetric
import com.cepgamer.strattester.strategy.BaseStrategy
import com.cepgamer.strattester.trader.BaseTrader
import com.cepgamer.strattester.trader.ProfitLossLockTrader

object TraderGenerator {

    private fun generateStrategyTraders(List<() -> BaseStrategy>): List<() -> BaseTrader> {

    }

    private fun generatePLCutoffTraders(metric: () -> BaseMetric): List<() -> BaseStrategy> {
        val list =
            ProfitLossLockTrader.generateNbyMTraders(
                metric(),
                moneyAvailable(),
                listOf(1, 2, 3, 4, 5),
                listOf(1, 2, 3, 4, 5),
                listOf(5, 10, 25, 100, 200)
            )
        return list
    }
}
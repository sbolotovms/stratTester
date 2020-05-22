package com.cepgamer.strattester.generator

import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.strategy.BaseStrategy
import com.cepgamer.strattester.trader.BaseTrader
import com.cepgamer.strattester.trader.ProfitLossLockTrader
import com.cepgamer.strattester.trader.StrategyTrader

class TraderGenerator(
    val strats: List<() -> BaseStrategy>,
    val havePLCutoffs: Boolean = false
) : Generator<BaseTrader> {

    fun moneyAvailable(): Dollar {
        return Dollar(10_000)
    }

    private fun generateStrategyTraders(): List<() -> BaseTrader> {
        val list = strats.map {
            {
                StrategyTrader(it(), moneyAvailable())
            }
        }

        return list
    }

    private fun generatePLCutoffTraders(): List<() -> BaseTrader> {
        val list = strats.map {
            ProfitLossLockTrader.generateNbyMTraders(
                it(),
                moneyAvailable(),
                listOf(1, 2, 3, 4, 5),
                listOf(1, 2, 3, 4, 5),
                listOf(5, 10, 25)
            )
        }.fold(emptyList()) { acc: List<() -> BaseTrader>, it: List<() -> BaseTrader> ->
            return@fold acc + it
        }

        return list
    }

    override fun generate(): List<BaseTrader> {
        val baseList = generateStrategyTraders()
        val plList = if (havePLCutoffs)
            generatePLCutoffTraders()
        else
            emptyList()
        return (baseList + plList).map { it() }
    }
}
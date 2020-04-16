package com.cepgamer.strattester.runner

import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.strategy.BaseStrategy
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

abstract class StrategyRunner(val strategies: List<BaseStrategy>) {
    fun updateStrategies(securities: List<Pair<BaseSecurity, PriceCandle>>) = runBlocking {
        strategies.map {
            launch {
                for (pair in securities.take(securities.size - 1)) {
                    it.priceUpdate(pair.second)
                }
                it.closePosition(securities.last().second)
            }
        }
    }

    fun closeStrategies(priceCandle: PriceCandle) = runBlocking {
        strategies.map {
            launch {
                it.closePosition(priceCandle)
            }
        }
    }

    abstract fun run()
}
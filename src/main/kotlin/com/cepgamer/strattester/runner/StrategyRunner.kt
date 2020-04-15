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
                for (pair in securities) {
                    it.priceUpdate(pair.first, pair.second)
                }
            }
        }
    }

    abstract fun run()
}
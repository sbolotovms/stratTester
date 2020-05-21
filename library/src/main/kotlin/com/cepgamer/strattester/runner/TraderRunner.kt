package com.cepgamer.strattester.runner

import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.strategy.BaseStrategy
import com.cepgamer.strattester.trader.BaseTrader
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

abstract class TraderRunner(val traders: List<BaseTrader>) {
    fun updateTraders(securities: List<Pair<BaseSecurity, PriceCandle>>) = runBlocking {
        traders.map {
            launch {
                for (pair in securities.take(securities.size - 1)) {
                    it.priceUpdate(pair.second)
                }
                it.closePositions(securities.last().second)
            }
        }
    }

    fun closePositions(priceCandle: PriceCandle) = runBlocking {
        traders.map {
            launch {
                it.closePositions(priceCandle)
            }
        }
    }

    abstract fun run()
}
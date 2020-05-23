package com.cepgamer.strattester.runner

import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.trader.BaseTrader
import com.cepgamer.strattester.util.StratLogger
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

abstract class TraderRunner(private val traders: List<BaseTrader>) {

    fun updateTraders(securities: List<Pair<Stock, PriceCandle>>) {
        val counter = AtomicLong(0)
        val percentCounter = AtomicLong(0)
        val provideUpdate = AtomicBoolean(true)
        val total = traders.size * securities.size
        traders.shuffled().chunked(traders.size / 10).map {
            runBlocking {
                it.map {
                    launch {
                        for (pair in securities.take(securities.size - 1)) {
                            it.priceUpdate(pair.second)
                            counter.incrementAndGet()
                        }
                        it.closePositions(securities.last().second)
                        if (provideUpdate.getAndSet(false)) {
                            StratLogger.i("Completed ${counter.get()} updates out of $total; ${percentCounter.get()} %")
                            System.gc()
                        } else {
                            val old = percentCounter.get()
                            percentCounter.set(counter.get() * 100 / total)
                            provideUpdate.set(old / 10 != percentCounter.get() / 10)
                        }
                    }
                }
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
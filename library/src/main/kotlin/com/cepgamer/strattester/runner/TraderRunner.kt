package com.cepgamer.strattester.runner

import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.trader.BaseTrader
import com.cepgamer.strattester.util.StratLogger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

abstract class TraderRunner(
    private val traders: List<BaseTrader>,
    val keepBad: Boolean = false
) {

    fun updateTraders(stockList: List<Pair<Stock, PriceCandle>>) {
        val counter = AtomicLong(0)
        val percentCounter = AtomicLong(0)
        val provideUpdate = AtomicBoolean(true)
        val total = traders.size * stockList.size
        traders.shuffled().chunked(traders.size / 20).map {
            it.map { trader ->
                GlobalScope.launch {
                    for (pair in stockList.take(stockList.size - 1)) {
                        trader.priceUpdate(pair.second)
                        counter.incrementAndGet()
                    }
                    trader.closePositions(stockList.last().second)
                    if (provideUpdate.getAndSet(false)) {
                        provideUpdate(counter.get(), total.toLong())
                    } else {
                        val old = percentCounter.get()
                        percentCounter.set(counter.get() * 100 / total)
                        provideUpdate.set(old / 10 != percentCounter.get() / 10)
                    }
                }
            }

            if (!keepBad) {
                it.filter { trader ->
                    trader.money > trader.startMoney
                }
            } else {
                it
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

    private fun provideUpdate(
        current: Long,
        total: Long
    ) {
        StratLogger.i("Completed $current updates out of $total; ${current * 100L / total} %")
        System.gc()
        val totalMem = Runtime.getRuntime().totalMemory()
        val usedMem = totalMem - Runtime.getRuntime().freeMemory()
        val trailingSpaces = " ".repeat(totalMem.toString().length - usedMem.toString().length)
        StratLogger.i("Memory use:      $trailingSpaces$usedMem")
        StratLogger.i("Total memory:    $totalMem")
    }

    abstract fun run()
}
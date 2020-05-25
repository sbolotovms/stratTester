package com.cepgamer.strattester.runner

import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.trader.BaseTrader
import com.cepgamer.strattester.util.StratLogger
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

abstract class TraderRunner(
    private val traders: List<BaseTrader>,
    val keepBad: Boolean = false
) {
    val counter = AtomicLong(0)
    val percentCounter = AtomicLong(0)
    val provideUpdate = AtomicBoolean(true)

    val chunkSize = 100_000

    fun updateTraders(stockList: List<Pair<Stock, PriceCandle>>): List<BaseTrader> {
        val total = traders.size * stockList.size
        val jobs = traders.shuffled().map { trader ->
            startTraderTestingAsync(stockList, trader, total.toLong())
        }

        val res = runBlocking {
            jobs.fold(Collections.synchronizedList(mutableListOf())) { acc: MutableList<BaseTrader>, job: Deferred<BaseTrader> ->
                acc += job.await()
                acc
            }
        }

        StratLogger.i("Finished running traders")

        return res
    }

    private fun startTraderTestingAsync(
        stockList: List<Pair<Stock, PriceCandle>>,
        trader: BaseTrader,
        total: Long
    ): Deferred<BaseTrader> =
        GlobalScope.async {
            runTrader(trader, stockList, total)
        }

    private fun runTrader(
        trader: BaseTrader,
        stockList: List<Pair<Stock, PriceCandle>>,
        total: Long
    ): BaseTrader {
        val loopThrough = stockList.take(stockList.size - 1)
        for (pair in loopThrough) {
            trader.priceUpdate(pair.second)
            counter.incrementAndGet()
        }
        trader.closePositions(stockList.last().second)
        counter.incrementAndGet()

        if (provideUpdate.getAndSet(false)) {
            provideUpdate(counter.get(), total)
        } else {
            val old = percentCounter.get()
            percentCounter.set(counter.get() * 100 / total)
            provideUpdate.set(old / 10 != percentCounter.get() / 10)
        }
        return trader
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
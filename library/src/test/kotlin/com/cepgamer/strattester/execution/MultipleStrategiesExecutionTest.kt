package com.cepgamer.strattester.execution

import com.cepgamer.strattester.TestConstants
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle

/**
 * Test to verify multiple strategies being executed properly w/o race conditions getting in the way.
 */
class MultipleStrategiesExecutionTest {
    val listOfCandles =
        (1..50).map {
            listOf(TestConstants.growthCandle, TestConstants.shrinkCandle)
        }.reduce(List<PriceCandle>::plus)

    private val money
        get() = Dollar(10_000)

    fun getAssertionTraders(
        candles: List<PriceCandle>,
        count: Int = 200
    ): List<AssertPriceCandleTrader> {
        return (1..count).map {
            AssertPriceCandleTrader(candles, money)
        }
    }

    fun `Test regular list of candles`() {
        val traders = getAssertionTraders(listOfCandles)

        TraderTestingExecutor(

        )
    }
}
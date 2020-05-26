package com.cepgamer.strattester.execution

import com.cepgamer.strattester.TestConstants
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.trader.BaseTrader
import org.junit.After
import org.junit.Assert
import org.junit.Test

/**
 * Test to verify multiple strategies being executed properly w/o race conditions getting in the way.
 */
class MultipleStrategiesExecutionTest {
    val listOfCandles =
        (1..50).map {
            listOf(TestConstants.growthCandle, TestConstants.shrinkCandle)
        }.reduce { a, b -> a + b }

    val tradersCount = 200

    private val money
        get() = Dollar(10_000)

    fun getAssertionTraders(
        candles: List<PriceCandle>,
        count: Int
    ): List<() -> AssertPriceCandleTrader> {
        return (1..count).map {
            { AssertPriceCandleTrader(candles, money) }
        }
    }

    @After
    fun tearDown() {
        TestConstants.verifyCandles()
    }

    @Test
    fun `Test regular list of candles`() {
        runHourlyData(listOfCandles) {
            Assert.assertEquals(Dollar(5_000), it.money)
            Assert.assertEquals(listOfCandles.size / 3, it.positions.size)
        }
    }

    @Test
    fun `Test shuffled list of candles`() {
        runHourlyData(listOfCandles.shuffled())
    }

    fun runHourlyData(
        candles: List<PriceCandle>,
        additionalChecks: (BaseTrader) -> Unit = {}
    ) {
        val traders = getAssertionTraders(candles, tradersCount)

        TraderTestingExecutor(
            "any",
            ArrayList(candles),
            traders
        ).apply {
            reportCallback = object : BaseExecutor.ResultReportCallback {
                override fun onResultTraders(traders: List<BaseTrader>, successfulCriteria: Dollar, runName: String) {
                    Assert.assertNotNull(traders)
                    Assert.assertEquals(tradersCount, traders.size)
                    for (trader in traders) {
                        Assert.assertEquals(candles.size / 3 * 2, trader.transactions.size)
                        Assert.assertEquals(Dollar(10_000), trader.startMoney)
                        Assert.assertEquals(0, trader.openPositions.size)

                        additionalChecks(trader)
                    }
                }
            }
            runHourlyData(candles)
        }
    }
}
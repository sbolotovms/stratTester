package com.cepgamer.strattester.trader

import com.cepgamer.strattester.TestConstants
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.strategy.BaseStrategy
import com.cepgamer.strattester.strategy.BlankStrategy
import io.mockk.every
import io.mockk.spyk
import org.junit.After
import org.junit.Assert
import org.junit.Test

class TraderTests {
    val money
        get() = Dollar(10_000)

    @After
    fun tearDown() {
        TestConstants.verifyCandles()
    }

    @Test
    fun `Test NoOpTrader`() {
        val trader = NoOpTrader(money)

        trader.priceUpdate(TestConstants.growthCandle)
        Assert.assertEquals(money, trader.money)

        trader.priceUpdate(TestConstants.shrinkCandle)
        Assert.assertEquals(money, trader.money)
    }

    @Test
    fun `Test StrategyTrader`() {
        val strategy = spyk(BlankStrategy(Stock("DIS")))
        every { strategy.priceUpdate(TestConstants.shrinkCandle) } returns BaseStrategy.Action.BUY
        every { strategy.priceUpdate(TestConstants.growthCandle) } returns BaseStrategy.Action.SELL
        val trader = StrategyTrader(strategy, money)

        // Buy
        trader.priceUpdate(TestConstants.shrinkCandle)
        Assert.assertEquals(Dollar(0), trader.money)
        Assert.assertTrue(trader.transactions.isNotEmpty())

        // Sell
        trader.priceUpdate(TestConstants.growthCandle)
        Assert.assertEquals(Dollar(20_000), trader.money)
        Assert.assertTrue(trader.transactions.isNotEmpty())

        // Buy again
        trader.priceUpdate(TestConstants.shrinkCandle)
        Assert.assertEquals(Dollar(0), trader.money)
        Assert.assertTrue(trader.transactions.isNotEmpty())
    }

    @Test
    fun `Test ProfitLossLockTrader`() {
        val strategy = spyk(BlankStrategy(Stock("DIS")))
        every { strategy.priceUpdate(TestConstants.shrinkCandle) } returns BaseStrategy.Action.BUY
        every { strategy.priceUpdate(TestConstants.growthCandle) } returns BaseStrategy.Action.SELL
        // Don't wait after sale
        val trader = ProfitLossLockTrader(strategy, money, sinceLastSaleWait = 0)

        // Buy
        trader.priceUpdate(TestConstants.shrinkCandle)
        Assert.assertEquals(Dollar(0), trader.money)
        Assert.assertTrue(trader.transactions.isNotEmpty())

        // Sell at a profit
        trader.priceUpdate(TestConstants.growthCandle)
        Assert.assertEquals(Dollar(20_000), trader.money)
        Assert.assertTrue(trader.transactions.isNotEmpty())

        // Buy again
        trader.priceUpdate(TestConstants.shrinkCandle)
        Assert.assertEquals(Dollar(0), trader.money)
        Assert.assertTrue(trader.transactions.isNotEmpty())

        // Sell at a loss
        trader.priceUpdate(TestConstants.shrinkCandle.copy(
            close = Dollar("0.8")))
        Assert.assertEquals(Dollar(16_000), trader.money)
        Assert.assertTrue(trader.transactions.isNotEmpty())
    }
}
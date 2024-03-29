package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.TestConstants
import com.cepgamer.strattester.metric.ConstantMetric
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.Position
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.security.Transaction
import com.cepgamer.strattester.trader.StrategyTrader
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class MetricCutoffStrategyTest {
    val security = Stock("AAPL")

    val money
        get() = Dollar(10_000)

    @Test
    fun `Test metric cutoff positive metric`() {
        val metric = ConstantMetric(BigDecimal.ONE, BigDecimal.ZERO)
        val strategy = MetricCutoffStrategy(metric, security)
        val trader = StrategyTrader(strategy, money)

        trader.priceUpdate(TestConstants.growthCandle)
        Assert.assertEquals(
            listOf(
                Transaction(
                    security,
                    BigDecimal(5_000).setScale(5),
                    Transaction.Action.BUY,
                    Date(TestConstants.growthCandle.openTimestamp + TestConstants.growthCandle.timespan)
                )
            ),
            trader.transactions
        )
        Assert.assertEquals(
            setOf(
                Position(
                    security,
                    BigDecimal(5_000).setScale(5),
                    Dollar(BigDecimal(2)),
                    trader.openPositions[0].purchaseDate,
                    Position.Status.OPEN
                )
            ), trader.positions
        )
    }

    @Test
    fun `Test metric cutoff negative metric`() {
        val metric = ConstantMetric(BigDecimal(-1), BigDecimal.ONE)
        val strategy = MetricCutoffStrategy(metric, security)
        val trader = StrategyTrader(strategy, money)

        strategy.priceUpdate(TestConstants.growthCandle)
        Assert.assertEquals(
            emptyList<Transaction>(),
            trader.transactions
        )
    }
}
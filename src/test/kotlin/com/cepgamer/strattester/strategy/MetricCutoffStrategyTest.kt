package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.TestConstants
import com.cepgamer.strattester.metric.ConstantMetric
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.Position
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.security.Transaction
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal

class MetricCutoffStrategyTest {
    val security = Stock("AAPL")

    val money
        get() = Dollar(10_000)

    @Test
    fun `Test metric cutoff positive metric`() {
        val metric = ConstantMetric(BigDecimal.ONE, BigDecimal.ZERO)
        val money = money
        val strategy = MetricCutoffStrategy(metric, security, money)

        strategy.priceUpdate(TestConstants.growthCandle)
        Assert.assertEquals(
            listOf(Transaction(security, BigDecimal(5_000), Transaction.Action.BUY)),
            strategy.transactions
        )
        Assert.assertEquals(
            listOf(
                Position(
                    security,
                    BigDecimal(5_000),
                    BigDecimal(2).setScale(5),
                    strategy.openPositions[0].purchaseDate,
                    Position.Status.OPEN
                )
            ), strategy.positions
        )
    }

    @Test
    fun `Test metric cutoff negative metric`() {
        val metric = ConstantMetric(BigDecimal(-1), BigDecimal.ONE)
        val money = money
        val strategy = MetricCutoffStrategy(metric, security, money)

        strategy.priceUpdate(TestConstants.growthCandle)
        Assert.assertEquals(
            emptyList<Transaction>(),
            strategy.transactions
        )
    }
}
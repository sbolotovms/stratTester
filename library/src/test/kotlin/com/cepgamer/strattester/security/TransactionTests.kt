package com.cepgamer.strattester.security

import com.cepgamer.strattester.TestConstants
import com.cepgamer.strattester.trader.NoOpTrader
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class TransactionTests {
    private val security = Stock("AAL")

    private val money
        get() = Dollar(10_000)

    @Test
    fun `Test transaction purchase`() {
        val trader = NoOpTrader(money)
        val (transaction, position) = Transaction.purchase(security, TestConstants.growthCandle, trader, trader.money)

        Assert.assertEquals(Dollar(BigDecimal.ZERO.setScale(5)), trader.money)
        Assert.assertEquals(Transaction(security, BigDecimal(5_000).setScale(5), Transaction.Action.BUY), transaction)
        Assert.assertEquals(
            Position(
                security,
                BigDecimal(5_000).setScale(5),
                BigDecimal(2).setScale(5),
                position.purchaseDate,
                Position.Status.OPEN
            ), position
        )
    }

    @Test
    fun `Test transaction sell`() {
        val trader = NoOpTrader(money)
        val position = Position(security, BigDecimal(10_000), BigDecimal(1).setScale(5), Date(), Position.Status.OPEN)
        val (transaction, resultingPosition) = Transaction.sell(position, TestConstants.growthCandle, trader)

        Assert.assertEquals(Dollar(BigDecimal(20_000).setScale(5)), trader.money)
        Assert.assertSame(position, resultingPosition)
        Assert.assertEquals(Transaction(security, BigDecimal(10_000), Transaction.Action.SELL), transaction)
        Assert.assertEquals(
            Position(
                security,
                BigDecimal(10_000),
                BigDecimal.ONE.setScale(5),
                resultingPosition.purchaseDate,
                Position.Status.CLOSED
            ).apply { sellPrice = BigDecimal(1).setScale(5) }, resultingPosition
        )
    }
}
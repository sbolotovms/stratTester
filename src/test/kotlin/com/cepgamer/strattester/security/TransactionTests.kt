package com.cepgamer.strattester.security

import com.cepgamer.strattester.TestConstants
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal
import java.nio.channels.ClosedSelectorException
import java.util.*

class TransactionTests {
    private val security = Stock("AAL")

    private val money
        get() = Dollar(10_000)

    @Test
    fun `Test transaction purchase`() {
        val money = money
        val (transaction, position) = Transaction.purchase(security, TestConstants.growthCandle, money)

        Assert.assertEquals(BigDecimal.ZERO, money.quantity)
        Assert.assertEquals(Transaction(security, BigDecimal(5_000), Transaction.Action.BUY), transaction)
        Assert.assertEquals(
            Position(
                security,
                BigDecimal(5_000),
                BigDecimal(2),
                position.purchaseDate,
                Position.Status.OPEN
            ), position
        )
    }

    @Test
    fun `Test transaction sell`() {
        val money = money
        val position = Position(security, BigDecimal(10_000), BigDecimal(1), Date(), Position.Status.OPEN)
        val (transaction, resultingPosition) = Transaction.sell(position, TestConstants.growthCandle, money)

        Assert.assertEquals(BigDecimal(20_000), money.quantity)
        Assert.assertSame(position, resultingPosition)
        Assert.assertEquals(Transaction(security, BigDecimal(10_000), Transaction.Action.SELL), transaction)
        Assert.assertEquals(
            Position(
                security,
                BigDecimal(10_000),
                BigDecimal.ONE,
                resultingPosition.purchaseDate,
                Position.Status.CLOSED
            ), resultingPosition
        )
    }
}
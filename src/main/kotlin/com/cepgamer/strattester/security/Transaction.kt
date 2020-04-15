package com.cepgamer.strattester.security

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

data class Transaction(val security: BaseSecurity, val quantity: BigDecimal, val action: Action) {
    enum class Action {
        BUY,
        SELL
    }

    class TransactionFailedException(reason: String) : Exception("Transaction failed, reason: $reason")

    companion object {
        @Throws(TransactionFailedException::class)
        fun purchase(security: BaseSecurity, priceCandle: PriceCandle, money: Dollar): Pair<Transaction, Position> {
            if (priceCandle.high >= money.quantity) {
                throw TransactionFailedException("Not enough money")
            }

            val quantity = money.quantity.divide(priceCandle.high, RoundingMode.FLOOR)
            money.quantity -= priceCandle.high * quantity

            return Transaction(security, quantity, Action.BUY) to Position(
                security,
                quantity,
                priceCandle.high,
                Date(priceCandle.openTimestamp.toLong()),
                Position.Status.OPEN
            )
        }

        fun sell(position: Position, priceCandle: PriceCandle, money: Dollar): Pair<Transaction, Position> {
            val moneyAcquired = priceCandle.low * position.quantity
            money.quantity += moneyAcquired
            position.apply {
                sellPrice = priceCandle.low
                status = Position.Status.CLOSED
            }
            return Transaction(position.security, position.quantity, Action.SELL) to position
        }
    }
}
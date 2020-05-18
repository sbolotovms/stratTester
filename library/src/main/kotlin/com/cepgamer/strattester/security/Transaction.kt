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
            if (priceCandle.close >= money.quantity) {
                throw TransactionFailedException("Not enough money")
            }

            val quantity = money.quantity.divide(priceCandle.close, RoundingMode.FLOOR).setScale(0, RoundingMode.FLOOR).setScale(5)
            money.quantity -= priceCandle.close * quantity
            money.quantity = money.quantity.setScale(5)

            return Transaction(security, quantity, Action.BUY) to Position(
                security,
                quantity,
                priceCandle.close,
                Date(priceCandle.openTimestamp.toLong()),
                Position.Status.OPEN
            )
        }

        fun sell(position: Position, priceCandle: PriceCandle, money: Dollar): Pair<Transaction, Position> {
            val moneyAcquired = priceCandle.close * position.quantity
            money.quantity += moneyAcquired
            position.apply {
                sellPrice = priceCandle.close
                status = Position.Status.CLOSED
            }
            return Transaction(position.security, position.quantity, Action.SELL) to position
        }
    }
}
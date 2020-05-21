package com.cepgamer.strattester.security

import com.cepgamer.strattester.trader.BaseTrader
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

data class Transaction(val security: Stock, val quantity: BigDecimal, val action: Action) {
    enum class Action {
        BUY,
        SELL
    }

    class TransactionFailedException(reason: String) : Exception("Transaction failed, reason: $reason")

    companion object {
        @Throws(TransactionFailedException::class)
        fun purchase(
            security: Stock,
            priceCandle: PriceCandle,
            trader: BaseTrader,
            money: Dollar
        ): Pair<Transaction, Position> {
            val buyPrice = priceCandle.buyPrice
            if (buyPrice >= money) {
                throw TransactionFailedException("Not enough money")
            }

            val quantity = money.divide(buyPrice, RoundingMode.FLOOR).setScale(0, RoundingMode.FLOOR).setScale(5)
            trader.money -= buyPrice * quantity

            return Transaction(security, quantity, Action.BUY) to Position(
                security,
                quantity,
                buyPrice,
                Date(priceCandle.openTimestamp),
                Position.Status.OPEN
            )
        }

        fun sell(position: Position, priceCandle: PriceCandle, trader: BaseTrader): Pair<Transaction, Position> {
            val sellingPrice = priceCandle.sellPrice
            val moneyAcquired = sellingPrice * position.quantity
            trader.money += moneyAcquired
            position.apply {
                sellPrice = sellingPrice
                status = Position.Status.CLOSED
            }
            return Transaction(position.security, position.quantity, Action.SELL) to position
        }
    }
}
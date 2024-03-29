package com.cepgamer.strattester.security

import com.cepgamer.strattester.trader.BaseTrader
import com.cepgamer.strattester.trader.TradingException
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

data class Transaction(val security: Stock, val quantity: BigDecimal, val action: Action, val timestamp: Date) {
    enum class Action {
        BUY,
        SELL
    }

    class TransactionFailedException(reason: String) : TradingException("Transaction failed, reason: $reason")

    companion object {
        @Throws(TransactionFailedException::class)
        fun purchase(
            security: Stock,
            priceCandle: PriceCandle,
            trader: BaseTrader,
            money: Dollar
        ): Pair<Transaction, Position> {
            synchronized(trader) {
                val buyPrice = priceCandle.buyPrice
                if (buyPrice >= money) {
                    throw TransactionFailedException("Not enough money")
                }

                val quantity = money.divide(buyPrice, RoundingMode.FLOOR).setScale(0, RoundingMode.FLOOR).setScale(5)
                trader.money -= buyPrice * quantity

                return Transaction(
                    security,
                    quantity,
                    Action.BUY,
                    Date(priceCandle.openTimestamp + priceCandle.timespan)
                ) to Position(
                    security,
                    quantity,
                    buyPrice,
                    Date(priceCandle.openTimestamp),
                    Position.Status.OPEN
                )
            }
        }

        fun sell(position: Position, priceCandle: PriceCandle, trader: BaseTrader): Pair<Transaction, Position> {
            synchronized(trader) {
                val sellingPrice = priceCandle.sellPrice
                trader.money += sellingPrice * position.quantity
                position.apply {
                    sellPrice = sellingPrice
                    status = Position.Status.CLOSED
                }
                return Transaction(
                    position.security,
                    position.quantity,
                    Action.SELL,
                    Date(priceCandle.openTimestamp + priceCandle.timespan)
                ) to position
            }
        }
    }
}
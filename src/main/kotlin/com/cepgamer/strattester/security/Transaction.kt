package com.cepgamer.strattester.security

import java.math.BigDecimal

class Transaction(val security: BaseSecurity, val quantity: BigDecimal, val action: Action) {
    enum class Action {
        BUY,
        SELL
    }
    class TransactionFailedException(reason: String) : Exception("Transaction failed, reason: $reason")

    companion object {
        fun purchase(security: BaseSecurity, priceCandle: PriceCandle, money: Dollar): Transaction {
            val quantity = money.quantity.rem(priceCandle.high)
            money.quantity -= priceCandle.high * quantity

            return Transaction(security, quantity, Action.BUY)
        }

        fun sell(security: BaseSecurity, priceCandle: PriceCandle, money: Dollar): Position {

        }
    }
}
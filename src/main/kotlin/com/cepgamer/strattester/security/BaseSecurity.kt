package com.cepgamer.strattester.security

import java.math.BigDecimal

abstract class BaseSecurity(val symbol: String) {
    @Throws(IllegalArgumentException::class)
    fun purchase(priceCandle: PriceCandle, quantity: BigDecimal, money: Dollar): Transaction {
        val price = priceCandle.high * quantity
        if (money.quantity < price) {
            throw IllegalArgumentException("Insufficient funds")
        }

        val ret = Transaction(this, quantity, Transaction.Action.BUY)
        money.quantity -= priceCandle.high * quantity

        return ret
    }
}

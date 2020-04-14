package com.cepgamer.strattester.security

import java.math.BigDecimal

class Transaction(val security: BaseSecurity, val quantity: BigDecimal) {
    class TransactionFailedException(reason: String) : Exception("Transaction failed, reason: $reason")

    fun purchase(priceCandle: PriceCandle) {

    }
}
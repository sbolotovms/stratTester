package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Transaction

class BuyStrategy(security: BaseSecurity, money: Dollar): BaseStrategy(security, money) {
    override fun priceUpdate(priceCandle: PriceCandle) {
        try {
            updateData(Transaction.purchase(security, priceCandle, moneyAvailable))
        } catch (e: Transaction.TransactionFailedException) {
            // ignore
        }
    }
}
package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Transaction
import java.util.concurrent.ConcurrentHashMap

abstract class BaseStrategy(val moneyAvailable: Dollar) {
    val securities = ConcurrentHashMap<BaseSecurity, List<Transaction>>()

    abstract fun priceUpdate(security: BaseSecurity, priceCandle: PriceCandle)
}
package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle

class BlankStrategy(money: Dollar) : BaseStrategy(money) {
    override fun priceUpdate(securities: List<Pair<BaseSecurity, PriceCandle>>) {
        // Ignore
    }
}
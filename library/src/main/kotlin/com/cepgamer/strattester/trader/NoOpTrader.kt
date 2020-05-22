package com.cepgamer.strattester.trader

import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle

class NoOpTrader(money: Dollar) : BaseTrader(money) {
    override fun priceUpdate(priceCandle: PriceCandle) {
        // do nothing
    }
}
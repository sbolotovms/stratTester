package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle

class BlankStrategy(
    security: BaseSecurity,
    money: Dollar
) : BaseStrategy(security, money) {
    override fun priceUpdate(
        priceCandle: PriceCandle
    ): Action {
        // Ignore
        return Action.HOLD
    }
}
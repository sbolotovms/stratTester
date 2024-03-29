package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.security.PriceCandle

class BlankStrategy(
    security: Stock
) : BaseStrategy(security) {
    override fun priceUpdate(
        priceCandle: PriceCandle
    ): Action {
        // Ignore
        return Action.HOLD
    }
}
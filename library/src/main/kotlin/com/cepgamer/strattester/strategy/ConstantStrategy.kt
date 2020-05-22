package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.security.PriceCandle

class ConstantStrategy(
    security: Stock,
    val action: Action
) : BaseStrategy(security) {
    override fun priceUpdate(priceCandle: PriceCandle): Action {
        return action
    }
}
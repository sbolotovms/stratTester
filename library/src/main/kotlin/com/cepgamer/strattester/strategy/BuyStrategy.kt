package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.PriceCandle

class BuyStrategy(security: BaseSecurity) : BaseStrategy(security) {
    override fun priceUpdate(priceCandle: PriceCandle): Action {
        return Action.BUY
    }
}
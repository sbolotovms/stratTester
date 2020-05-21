package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.security.PriceCandle

class BuyStrategy(security: Stock) : BaseStrategy(security) {
    override fun priceUpdate(priceCandle: PriceCandle): Action {
        return Action.BUY
    }
}
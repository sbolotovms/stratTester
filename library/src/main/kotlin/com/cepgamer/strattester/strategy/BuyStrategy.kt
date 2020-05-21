package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle

class BuyStrategy(security: BaseSecurity, money: Dollar) : BaseStrategy(security) {
    override fun priceUpdate(priceCandle: PriceCandle): Action {
        return purchaseStock(priceCandle)
    }
}
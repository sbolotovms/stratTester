package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle

class InverseStrategy(val strategy: BaseStrategy, security: BaseSecurity, money: Dollar) : BaseStrategy(
    security
) {
    override fun priceUpdate(priceCandle: PriceCandle): Action {
        val action = strategy.priceUpdate(priceCandle)
        if (action == Action.BUY) {
            return closePositions(priceCandle)
        } else if (action == Action.SELL) {
            return purchaseStock(priceCandle)
        }
        return Action.HOLD
    }

    override fun toString(): String {
        return super.toString() + """
            Underlying strategy: $strategy
        """
    }
}
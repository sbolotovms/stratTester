package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.PriceCandle

class InverseStrategy(
    val strategy: BaseStrategy,
    security: BaseSecurity
) : BaseStrategy(
    security
) {
    override fun priceUpdate(priceCandle: PriceCandle): Action {
        val action = strategy.priceUpdate(priceCandle)
        if (action == Action.BUY) {
            return Action.SELL
        } else if (action == Action.SELL) {
            return Action.BUY
        }
        return Action.HOLD
    }

    override fun toString(): String {
        return super.toString() + """
            Underlying strategy: $strategy
        """
    }
}
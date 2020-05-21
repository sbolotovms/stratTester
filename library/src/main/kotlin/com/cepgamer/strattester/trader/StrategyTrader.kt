package com.cepgamer.strattester.trader

import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.strategy.BaseStrategy

class StrategyTrader(val strategy: BaseStrategy, money: Dollar) : BaseTrader(money) {
    override fun priceUpdate(priceCandle: PriceCandle) {
        val action = strategy.priceUpdate(priceCandle)
        when (action)
        {
            BaseStrategy.Action.BUY ->
                purchaseStock(priceCandle,,)
            BaseStrategy.Action.SELL -> TODO()
            BaseStrategy.Action.HOLD -> TODO()
        }
    }
}
package com.cepgamer.strattester.trader

import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.strategy.BaseStrategy

open class StrategyTrader(val strategy: BaseStrategy, money: Dollar) : BaseTrader(money) {
    override fun priceUpdate(priceCandle: PriceCandle) {
        val action = strategy.priceUpdate(priceCandle)
        when (action)
        {
            BaseStrategy.Action.BUY ->
                purchaseStock(priceCandle, strategy.security, money)
            BaseStrategy.Action.SELL ->
                closePositions(priceCandle)
            BaseStrategy.Action.HOLD ->
                // ignore
                Unit
        }
    }

    override fun toString(): String {
        return super.toString() + """
            Underlying strategy: $strategy"""
    }
}
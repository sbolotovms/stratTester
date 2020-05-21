package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.security.*

abstract class BaseStrategy(
    val security: Stock
) {
    enum class Action {
        BUY,
        SELL,
        HOLD
    }

    abstract fun priceUpdate(
        priceCandle: PriceCandle
    ): Action

    override fun toString(): String {
        return """
            Type: ${this::class.simpleName}"""
    }
}
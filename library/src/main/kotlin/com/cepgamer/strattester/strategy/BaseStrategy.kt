package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.security.*
import com.cepgamer.strattester.util.StratLogger

abstract class BaseStrategy(
    val security: BaseSecurity
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
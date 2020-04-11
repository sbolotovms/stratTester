package com.cepgamer.strattester.metric

import com.cepgamer.strattester.security.PriceCandle
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.math.BigDecimal

class SimpleGrowthMetric(candles: List<PriceCandle>) : BaseMetric(candles) {
    init {
        if (candles.isEmpty()) {
            throw IllegalArgumentException("Price list is empty")
        }
    }

    val lastCandle = data[data.lastIndex]

    override val goodSignalInternal: BigDecimal
        get() = (lastCandle.close - lastCandle.open) / (lastCandle.high - lastCandle.low)
    override val badSignalInternal: BigDecimal
        get() = (lastCandle.open - lastCandle.close) / (lastCandle.high - lastCandle.low)
}
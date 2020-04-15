package com.cepgamer.strattester.metric

import java.math.BigDecimal

class SimpleGrowthMetric : BaseMetric() {
    private val lastCandle
        get() = data[data.lastIndex]

    override val goodSignalInternal: BigDecimal
        get() = (lastCandle.close - lastCandle.open) / (lastCandle.high - lastCandle.low)
    override val badSignalInternal: BigDecimal
        get() = (lastCandle.open - lastCandle.close) / (lastCandle.high - lastCandle.low)
}
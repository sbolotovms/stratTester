package com.cepgamer.strattester.metric

import java.math.BigDecimal

class SimpleGrowthMetric : BaseMetric() {
    private val lastCandle
        get() = data[data.lastIndex]

    override val goodSignalInternal: BigDecimal
        get() = lastCandle.openToCloseGrowth
    override val badSignalInternal: BigDecimal
        get() = -lastCandle.openToCloseGrowth
}

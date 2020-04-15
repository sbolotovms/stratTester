package com.cepgamer.strattester.metric

import java.math.BigDecimal

class InverseMetric(private val otherMetric: BaseMetric) : BaseMetric() {
    override val goodSignalInternal: BigDecimal
        get() = otherMetric.badSignal
    override val badSignalInternal: BigDecimal
        get() = otherMetric.goodSignal
}
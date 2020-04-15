package com.cepgamer.strattester.metric

import java.math.BigDecimal

class InverseMetric(otherMetric: BaseMetric): DelegateMetric(otherMetric) {
    override val goodSignalInternal: BigDecimal
        get() = BigDecimal.ONE - otherMetric.goodSignal
    override val badSignalInternal: BigDecimal
        get() = BigDecimal.ONE - otherMetric.badSignal
}
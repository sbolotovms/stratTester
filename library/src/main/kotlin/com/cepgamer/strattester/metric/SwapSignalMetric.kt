package com.cepgamer.strattester.metric

import java.math.BigDecimal

class SwapSignalMetric(otherMetric: BaseMetric) : DelegateMetric(otherMetric) {
    override val goodSignalInternal: BigDecimal
        get() = otherMetric.badSignal
    override val badSignalInternal: BigDecimal
        get() = otherMetric.goodSignal
}
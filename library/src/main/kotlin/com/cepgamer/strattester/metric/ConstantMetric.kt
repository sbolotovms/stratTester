package com.cepgamer.strattester.metric

import java.math.BigDecimal

class ConstantMetric(private val goodConstSignal: BigDecimal, private val badConstSignal: BigDecimal) : BaseMetric() {
    override val goodSignalInternal: BigDecimal
        get() = goodConstSignal
    override val badSignalInternal: BigDecimal
        get() = badConstSignal

}
package com.cepgamer.strattester.metric

import com.cepgamer.strattester.security.PriceCandle
import java.math.BigDecimal

class GrowthMetric(candles: List<PriceCandle>) : BaseMetric(candles) {
    override val goodSignalInternal: BigDecimal
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val badSignalInternal: BigDecimal
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}
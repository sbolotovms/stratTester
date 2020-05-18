package com.cepgamer.strattester.indicator

import com.cepgamer.strattester.security.PriceCandle
import java.math.BigDecimal

abstract class NumericIndicator(priceCandles: MutableList<PriceCandle>) : BaseIndicator(priceCandles) {
    abstract val indicatorValue: BigDecimal
}
package com.cepgamer.strattester.indicator

import com.cepgamer.strattester.security.PriceCandle
import java.math.BigDecimal

class MovingAverage(val dataRange: Int, priceCandles: MutableList<PriceCandle>) : NumericIndicator(priceCandles) {
    override val indicatorValue: BigDecimal
        get() = priceCandles.fold(
            BigDecimal(0).setScale(5),
            { acc: BigDecimal, priceCandle: PriceCandle -> acc + priceCandle.buyPrice }) / BigDecimal(priceCandles.size)

    override fun afterCandleListUpdate() {
        if (dataRange < priceCandles.size) {
            priceCandles.drop(priceCandles.size - dataRange)
        }
    }
}
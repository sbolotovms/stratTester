package com.cepgamer.strattester.indicator

import com.cepgamer.strattester.security.PriceCandle

abstract class BaseIndicator(priceCandlesArg: MutableList<PriceCandle>) {
    val priceCandles = ArrayList(priceCandlesArg)

    abstract fun afterCandleListUpdate()

    fun updateCandleList(newPriceCandles: List<PriceCandle>) {
        priceCandles.addAll(newPriceCandles)
        afterCandleListUpdate()
    }
}
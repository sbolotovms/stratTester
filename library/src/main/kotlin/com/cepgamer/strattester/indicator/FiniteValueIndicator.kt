package com.cepgamer.strattester.indicator

import com.cepgamer.strattester.security.PriceCandle

abstract class FiniteValueIndicator<T : Enum<T>>(priceCandlesArg: MutableList<PriceCandle>) : BaseIndicator(priceCandlesArg) {
    abstract val indicatorValue: T
}
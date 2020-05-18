package com.cepgamer.strattester.indicator

import com.cepgamer.strattester.security.PriceCandle

abstract class BuySellIndicator(priceCandlesArg: MutableList<PriceCandle>) : FiniteValueIndicator<BuySellIndicator.BuySell>(priceCandlesArg) {
    enum class BuySell {
        BUY, SELL
    }
}
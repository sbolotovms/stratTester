package com.cepgamer.strattester.execution

import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.trader.BaseTrader
import org.junit.Assert

class AssertPriceCandleTrader(
    iteratingCandles: List<PriceCandle>,
    money: Dollar
) : BaseTrader(money) {
    private val iteratingCandles = ArrayList(iteratingCandles)
    var index = 0
    override fun priceUpdate(priceCandle: PriceCandle) {
        Assert.assertEquals(iteratingCandles[index], priceCandle)

        index++
    }
}
package com.cepgamer.strattester.execution

import com.cepgamer.strattester.TestConstants
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Stock
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

        when (index % 3)
        {
            0 -> Unit
            1 -> closePositions(priceCandle)
            2 -> purchaseStock(priceCandle, Stock("TEST"), money)
        }

        TestConstants.verifyCandles()

        index++
    }
}
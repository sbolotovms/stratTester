package com.cepgamer.strattester

import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Stock
import org.junit.Assert

object TestConstants {
    val growthCandle = PriceCandle(
        1,
        2,
        1,
        2,
        5,
        100,
        60,
        Stock("SPY")
    )
    val shrinkCandle = PriceCandle(
        2,
        1,
        1,
        2,
        5,
        100,
        60,
        Stock("SPY")
    )

    fun verifyCandles() {
        val growthCandle = PriceCandle(
            1,
            2,
            1,
            2,
            5,
            100,
            60,
            Stock("SPY")
        )
        Assert.assertEquals(growthCandle, this.growthCandle)

        val shrinkCandle = PriceCandle(
            2,
            1,
            1,
            2,
            5,
            100,
            60,
            Stock("SPY")
        )
        Assert.assertEquals(shrinkCandle, this.shrinkCandle)
    }
}
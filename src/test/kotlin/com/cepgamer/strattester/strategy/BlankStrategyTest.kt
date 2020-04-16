package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.TestConstants.growthCandle
import com.cepgamer.strattester.TestConstants.shrinkCandle
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.Stock
import org.junit.Test

class BlankStrategyTest {
    val money = Dollar(1_000)
    val security = Stock("ALK")
    val blankStrategy = BlankStrategy(security, money)

    @Test
    fun `Test money didn't change`()
    {
        blankStrategy.priceUpdate(growthCandle)
        blankStrategy.priceUpdate(shrinkCandle)
    }
}
package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.TestConstants.growthCandle
import com.cepgamer.strattester.TestConstants.shrinkCandle
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.Stock
import org.junit.Test

class BlankStrategyTest {
    val money = Dollar(1_000)
    val security1 = Stock("ALK")
    val security2 = Stock("ALK")
    val blankStrategy = BlankStrategy(money)

    @Test
    fun `Test money didn't change`()
    {
        blankStrategy.priceUpdate(listOf(
            security1 to growthCandle,
            security2 to shrinkCandle
        ))
    }
}
package com.cepgamer.strattester.security

import org.junit.Assert
import org.junit.Test

class StockTests {
    private val symbol = "TSLA"
    val stock = Stock(symbol)

    @Test
    fun `Test stock`() {
        Assert.assertEquals(stock.symbol, symbol)
    }
}
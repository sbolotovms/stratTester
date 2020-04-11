package com.cepgamer.strattester.security

import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal

class DollarTest {
    @Test
    fun `Initial test`() {
        val dollar1 = Dollar(BigDecimal(10))
        val dollar2 = Dollar(BigDecimal(10))
        Assert.assertEquals(Dollar(BigDecimal(20)), dollar1 + dollar2)
    }

    @Test
    fun `Minus test`() {
        val dollar1 = Dollar(BigDecimal(40))
        val dollar2 = Dollar(BigDecimal(10))
        Assert.assertEquals(Dollar(BigDecimal(30)), dollar1 - dollar2)
    }
}
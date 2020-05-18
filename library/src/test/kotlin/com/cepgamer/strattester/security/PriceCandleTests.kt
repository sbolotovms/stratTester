package com.cepgamer.strattester.security

import com.cepgamer.strattester.TestConstants.growthCandle
import com.cepgamer.strattester.TestConstants.shrinkCandle
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal

class PriceCandleTests {
    @Test
    fun `Test growth candle`() {
        Assert.assertEquals(BigDecimal(1).setScale(5), growthCandle.open)
        Assert.assertEquals(BigDecimal(2).setScale(5), growthCandle.close)
        Assert.assertEquals(BigDecimal(2).setScale(5), growthCandle.high)
        Assert.assertEquals(BigDecimal(1).setScale(5), growthCandle.low)
        Assert.assertEquals(BigDecimal(5).setScale(5), growthCandle.volume)
        Assert.assertEquals(100, growthCandle.openTimestamp)
        Assert.assertEquals(60, growthCandle.timespan)
    }
    @Test
    fun `Test shrink candle`() {
        Assert.assertEquals(BigDecimal(2).setScale(5), shrinkCandle.open)
        Assert.assertEquals(BigDecimal(1).setScale(5), shrinkCandle.close)
        Assert.assertEquals(BigDecimal(2).setScale(5), shrinkCandle.high)
        Assert.assertEquals(BigDecimal(1).setScale(5), shrinkCandle.low)
        Assert.assertEquals(BigDecimal(5).setScale(5), shrinkCandle.volume)
        Assert.assertEquals(100, shrinkCandle.openTimestamp)
        Assert.assertEquals(60, shrinkCandle.timespan)
    }
}
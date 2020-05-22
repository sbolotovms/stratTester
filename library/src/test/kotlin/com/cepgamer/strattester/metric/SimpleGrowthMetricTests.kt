package com.cepgamer.strattester.metric

import com.cepgamer.strattester.TestConstants.growthCandle
import com.cepgamer.strattester.TestConstants.shrinkCandle
import com.cepgamer.strattester.security.Dollar
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal

class SimpleGrowthMetricTests {
    lateinit var simpleGrowthMetric: SimpleGrowthMetric

    @Test
    fun `Test growth result value`() {
        simpleGrowthMetric = SimpleGrowthMetric().apply { newData(growthCandle) }

        Assert.assertEquals(BigDecimal(1).setScale(5), simpleGrowthMetric.goodSignal)
        Assert.assertEquals(BigDecimal(-1).setScale(5), simpleGrowthMetric.badSignal)
    }

    @Test
    fun `Test shrink result value`() {
        simpleGrowthMetric = SimpleGrowthMetric().apply { newData(shrinkCandle) }

        Assert.assertEquals(BigDecimal(-1).setScale(5), simpleGrowthMetric.goodSignal)
        Assert.assertEquals(BigDecimal(1).setScale(5), simpleGrowthMetric.badSignal)
    }

    @Test
    fun `Test half growth`() {
        val halfCandle = growthCandle.copy(close = Dollar(BigDecimal(1.5)))
        simpleGrowthMetric = SimpleGrowthMetric().apply { newData(halfCandle) }

        Assert.assertEquals(BigDecimal(0.5).setScale(5), simpleGrowthMetric.goodSignal)
        Assert.assertEquals(BigDecimal(-0.5).setScale(5), simpleGrowthMetric.badSignal)
    }
}
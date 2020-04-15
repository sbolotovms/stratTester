package com.cepgamer.strattester.metric

import com.cepgamer.strattester.TestConstants.growthCandle
import com.cepgamer.strattester.TestConstants.shrinkCandle
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal

class SimpleGrowthMetricTests {

    lateinit var simpleGrowthMetric: SimpleGrowthMetric

    @Test
    fun `Test growth result value`() {
        simpleGrowthMetric = SimpleGrowthMetric(listOf(growthCandle))

        Assert.assertEquals(BigDecimal(1), simpleGrowthMetric.goodSignal)
        Assert.assertEquals(BigDecimal(-1), simpleGrowthMetric.badSignal)
    }

    @Test
    fun `Test shrink result value`() {
        simpleGrowthMetric = SimpleGrowthMetric(listOf(shrinkCandle))

        Assert.assertEquals(BigDecimal(-1), simpleGrowthMetric.goodSignal)
        Assert.assertEquals(BigDecimal(1), simpleGrowthMetric.badSignal)
    }
}
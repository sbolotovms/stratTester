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

        Assert.assertTrue(BigDecimal(1).compareTo(simpleGrowthMetric.goodSignal) == 0)
        Assert.assertTrue(BigDecimal(-1).compareTo(simpleGrowthMetric.badSignal) == 0)
    }

    @Test
    fun `Test shrink result value`() {
        simpleGrowthMetric = SimpleGrowthMetric().apply { newData(shrinkCandle) }

        Assert.assertTrue(BigDecimal(-1).compareTo(simpleGrowthMetric.goodSignal) == 0)
        Assert.assertTrue(BigDecimal(1).compareTo(simpleGrowthMetric.badSignal) == 0)
    }

    @Test
    fun `Test half growth`() {
        val halfCandle = growthCandle.copy(close = Dollar(BigDecimal(1.5)))
        simpleGrowthMetric = SimpleGrowthMetric().apply { newData(halfCandle) }

        Assert.assertTrue(BigDecimal(0.5).compareTo(simpleGrowthMetric.goodSignal) == 0)
        Assert.assertTrue(BigDecimal(-0.5).compareTo(simpleGrowthMetric.badSignal) == 0)
    }

    @Test
    fun `Test 5th precision point`() {
        val halfCandle = growthCandle.copy(close = Dollar(BigDecimal("1.1234")))
        simpleGrowthMetric = SimpleGrowthMetric().apply { newData(halfCandle) }

        Assert.assertTrue(BigDecimal("0.1234").compareTo(simpleGrowthMetric.goodSignal) == 0)
        Assert.assertTrue(BigDecimal("-0.1234").compareTo(simpleGrowthMetric.badSignal) == 0)
    }
}
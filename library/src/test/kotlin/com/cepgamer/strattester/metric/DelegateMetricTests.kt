package com.cepgamer.strattester.metric

import com.cepgamer.strattester.TestConstants
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import java.math.BigDecimal

class DelegateMetricTests {
    class MockedDelegateMetric(baseMetric: BaseMetric) : DelegateMetric(baseMetric) {
        override val goodSignalInternal: BigDecimal
            get() = BigDecimal.ZERO
        override val badSignalInternal: BigDecimal
            get() = BigDecimal.ZERO
    }

    @Test
    fun `Test delegation works`() {
        val mockMetric = mockk<BaseMetric>(relaxed = true)
        val delegate = MockedDelegateMetric(mockMetric)

        delegate.newData(TestConstants.growthCandle)
        verify(exactly = 1) { mockMetric.newData(TestConstants.growthCandle) }
    }
}
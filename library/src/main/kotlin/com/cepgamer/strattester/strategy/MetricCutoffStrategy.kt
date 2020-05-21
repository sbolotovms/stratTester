package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.metric.BaseMetric
import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import java.math.BigDecimal

open class MetricCutoffStrategy(
    metric: BaseMetric,
    security: BaseSecurity,
    val goodSignalCutoff: BigDecimal = BigDecimal.ZERO,
    val badSignalCutoff: BigDecimal = BigDecimal.ZERO
) : MetricStrategy(metric, security) {
    override fun priceUpdate(
        priceCandle: PriceCandle
    ): Action {
        metric.newData(priceCandle)

        if (metric.goodSignal >= goodSignalCutoff) {
            return Action.BUY
        } else if (metric.badSignal >= badSignalCutoff) {
            return Action.SELL
        }
        return Action.HOLD
    }

    override fun toString(): String {
        return super.toString() + """
            Good signal cutoff: $goodSignalCutoff
            Bad signal cutoff: $badSignalCutoff
        """
    }

    companion object {
        /**
         * Generates (N + 1) * (M + 1) strategies from 0 to 1 with 1/N step for good signal and 1/M step for bad signal.
         */
        fun generateNbyMStrategies(
            metric: BaseMetric,
            security: BaseSecurity,
            moneyAvailable: Dollar,
            n: Int,
            m: Int
        ): List<() -> BaseStrategy> {
            return (0..n).map { i ->
                (0..m).map { j ->
                    {
                        MetricCutoffStrategy(
                            metric,
                            security,
                            BigDecimal(i) / BigDecimal(n),
                            BigDecimal(j) / BigDecimal(n)
                        )
                    }
                }
            }.reduce { list1: List<() -> MetricCutoffStrategy>, list2: List<() -> MetricCutoffStrategy> ->
                list1 + list2
            }
        }
    }
}
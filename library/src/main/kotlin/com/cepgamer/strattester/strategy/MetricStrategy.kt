package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.metric.BaseMetric
import com.cepgamer.strattester.security.Stock

abstract class MetricStrategy(
    val metric: BaseMetric,
    security: Stock
) : BaseStrategy(
    security
) {
    override fun toString(): String {
        return super.toString() + """
            Underlying metric: $metric
        """
    }
}
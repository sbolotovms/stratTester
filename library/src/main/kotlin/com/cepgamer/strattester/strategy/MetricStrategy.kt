package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.metric.BaseMetric
import com.cepgamer.strattester.security.BaseSecurity

abstract class MetricStrategy(
    val metric: BaseMetric,
    security: BaseSecurity
) : BaseStrategy(
    security
) {
    override fun toString(): String {
        return super.toString() + """
            Underlying metric: $metric
        """
    }
}
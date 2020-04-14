package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.metric.BaseMetric
import com.cepgamer.strattester.security.Dollar

abstract class MetricStrategy(val metric: BaseMetric, moneyAvailable: Dollar) : BaseStrategy(moneyAvailable) {
}
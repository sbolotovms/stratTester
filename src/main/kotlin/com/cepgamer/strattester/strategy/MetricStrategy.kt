package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.metric.BaseMetric
import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar

abstract class MetricStrategy(val metric: BaseMetric, security: BaseSecurity, moneyAvailable: Dollar) : BaseStrategy(security, moneyAvailable)
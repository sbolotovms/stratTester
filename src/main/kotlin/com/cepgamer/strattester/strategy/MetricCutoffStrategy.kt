package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.metric.BaseMetric
import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle

class MetricCutoffStrategy(metric: BaseMetric, moneyAvailable: Dollar) : MetricStrategy(metric, moneyAvailable) {
    override fun priceUpdate(securities: List<Pair<BaseSecurity, PriceCandle>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
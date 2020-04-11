package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.metric.BaseMetric
import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.PriceCandle

class BlankStrategy(metric: BaseMetric) : BaseStrategy(metric) {
    override fun priceUpdate(security: BaseSecurity, priceCandle: PriceCandle) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
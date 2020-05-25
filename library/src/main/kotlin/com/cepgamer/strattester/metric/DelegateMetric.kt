package com.cepgamer.strattester.metric

import com.cepgamer.strattester.security.PriceCandle

abstract class DelegateMetric(val otherMetric: BaseMetric) : BaseMetric() {
    override fun newData(priceCandle: PriceCandle) {
        otherMetric.newData(priceCandle)
        super.newData(priceCandle)
    }

    override fun toString(): String {
        return super.toString() + """
            underlying delegated metric: $otherMetric"""
    }
}
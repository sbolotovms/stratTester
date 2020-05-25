package com.cepgamer.strattester.metric

import com.cepgamer.strattester.security.PriceCandle
import java.math.BigDecimal

abstract class BaseMetric {
    val data: MutableList<PriceCandle> = mutableListOf()

    open fun newData(priceCandle: PriceCandle) {
        data.add(priceCandle)
    }

    protected abstract val goodSignalInternal: BigDecimal

    val goodSignal: BigDecimal
        get() {
            val signal = BigDecimal(goodSignalInternal.toPlainString())
            if (signal > BigDecimal(1) || signal < BigDecimal(-1)) {
                throw IllegalStateException("Good signal is not normalised")
            }

            return signal
        }

    override fun toString(): String {
        return "\nMetric: ${this::class.simpleName}"
    }

    protected abstract val badSignalInternal: BigDecimal

    val badSignal: BigDecimal
        get() {
            val signal = BigDecimal(badSignalInternal.toPlainString())
            if (signal > BigDecimal(1) || signal < BigDecimal(-1)) {
                throw IllegalStateException("Bad signal is not normalised")
            }

            return signal
        }
}
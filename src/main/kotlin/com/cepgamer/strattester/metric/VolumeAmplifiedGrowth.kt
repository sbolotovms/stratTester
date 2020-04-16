package com.cepgamer.strattester.metric

import java.math.BigDecimal

class VolumeAmplifiedGrowth(private val trailingPrices: Int) : BaseMetric() {
    init {
        assert(trailingPrices >= 1)
    }

    fun getSignal(isGood: Boolean): BigDecimal {
        if (data.size <= trailingPrices) {
            return BigDecimal.ZERO
        }
        val last = data.last()

        val averageVolume =
            data.takeLast(trailingPrices + 1).take(trailingPrices).fold(BigDecimal(0).setScale(5), { acc, priceCandle ->
                acc + priceCandle.volume
            }) / BigDecimal(trailingPrices).setScale(5)
        val priceMovement = (last.close - last.open) / (last.high - last.low)
        val untrimmedSignal = last.volume / averageVolume * priceMovement

        return if (isGood) {
            if (priceMovement >= BigDecimal.ZERO) {
                untrimmedSignal.min(BigDecimal.ONE)
            } else {
                BigDecimal.ZERO
            }
        } else {
            if (priceMovement >= BigDecimal.ZERO) {
                BigDecimal.ZERO
            } else {
                untrimmedSignal.min(BigDecimal.ONE)
            }
        }
    }

    override val goodSignalInternal: BigDecimal
        get() = getSignal(true)
    override val badSignalInternal: BigDecimal
        get() = getSignal(false)
}
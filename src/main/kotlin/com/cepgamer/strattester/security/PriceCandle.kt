package com.cepgamer.strattester.security

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

data class PriceCandle(
    var open: BigDecimal,
    var close: BigDecimal,
    var low: BigDecimal,
    var high: BigDecimal,
    var volume: BigDecimal,
    val openTimestamp: Int,
    val timespan: Int
) {
    constructor(
        open: Int,
        close: Int,
        low: Int,
        high: Int,
        volume: Int,
        openTimestamp: Int,
        timespan: Int
    ) : this(
        BigDecimal(open),
        BigDecimal(close),
        BigDecimal(low),
        BigDecimal(high),
        BigDecimal(volume),
        openTimestamp,
        timespan
    )

    init {
        open = open.setScale(5, RoundingMode.HALF_UP)
        close = close.setScale(5, RoundingMode.HALF_UP)
        low = low.setScale(5, RoundingMode.HALF_UP)
        high = high.setScale(5, RoundingMode.HALF_UP)
        volume = volume.setScale(5, RoundingMode.HALF_UP)
    }
}
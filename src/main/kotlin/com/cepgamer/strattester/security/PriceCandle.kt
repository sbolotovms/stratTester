package com.cepgamer.strattester.security

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

data class PriceCandle(
    open: BigDecimal,
    close: BigDecimal,
    low: BigDecimal,
    high: BigDecimal,
    volume: BigDecimal,
    val openTimestamp: Int,
    val timespan: Int
) {
    val open = open.setScale(5, RoundingMode.HALF_UP)
    val close = close.setScale(5, RoundingMode.HALF_UP)
    val low = low.setScale(5, RoundingMode.HALF_UP)
    val high = high.setScale(5, RoundingMode.HALF_UP)
    val volume = volume.setScale(5, RoundingMode.HALF_UP)

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
    }
}
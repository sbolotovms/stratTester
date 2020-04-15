package com.cepgamer.strattester.security

import java.math.BigDecimal
import java.math.BigInteger

data class PriceCandle(
    val open: BigDecimal,
    val close: BigDecimal,
    val low: BigDecimal,
    val high: BigDecimal,
    val volume: BigDecimal,
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
}
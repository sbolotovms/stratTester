package com.cepgamer.strattester.security

import java.math.BigDecimal
import java.math.BigInteger

data class PriceCandle(
    val open: BigDecimal,
    val close: BigDecimal,
    val low: BigDecimal,
    val high: BigDecimal,
    val volume: BigDecimal,
    val openTimestamp: BigInteger,
    val timespan: BigInteger
)
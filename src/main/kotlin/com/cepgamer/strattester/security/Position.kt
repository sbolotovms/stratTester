package com.cepgamer.strattester.security

import java.math.BigDecimal
import java.util.*

data class Position(
    val security: BaseSecurity,
    val quantity: BigDecimal,
    val purchasePrice: BigDecimal,
    val purchaseDate: Date
) {
}
package com.cepgamer.strattester.security

import java.math.BigDecimal
import java.util.*

data class Position(
    val security: Stock,
    val quantity: BigDecimal,
    val purchasePrice: Dollar,
    val purchaseDate: Date,
    var status: Status,
    var sellPrice: Dollar = Dollar(-1)
) {
    enum class Status {
        OPEN,
        CLOSED
    }
}
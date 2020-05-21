package com.cepgamer.strattester.security

import java.math.BigDecimal

class Dollar(quantity: String) : BigDecimal(quantity) {
    constructor(quantity: Int) : this(quantity.toString())

    override fun toString(): String {
        return "Dollar: ${super.toString()}"
    }

    override fun toByte(): Byte {
        return toInt().toByte()
    }

    override fun toChar(): Char {
        return toInt().toChar()
    }

    override fun toShort(): Short {
        return toInt().toShort()
    }
}
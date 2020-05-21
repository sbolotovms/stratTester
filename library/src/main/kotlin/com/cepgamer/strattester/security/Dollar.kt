package com.cepgamer.strattester.security

import java.math.BigDecimal

class Dollar(quantity: String) : BigDecimal(quantity) {
    constructor(quantity: Int) : this(quantity.toString())
    constructor(quantity: BigDecimal) : this(quantity.toString())

    init {
        setScale(5)
    }

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

    override fun min(other: BigDecimal?): Dollar {
        return Dollar(super.min(other))
    }

    override fun max(other: BigDecimal?): Dollar {
        return Dollar(super.max(other))
    }

    override fun multiply(multiplicand: BigDecimal?): Dollar {
        return Dollar(super.multiply(multiplicand))
    }

    operator fun plus(augend: BigDecimal?): Dollar {
        return Dollar(super.add(augend))
    }

    operator fun minus(subtrahend: BigDecimal?): Dollar {
        return Dollar(super.subtract(subtrahend))
    }

    operator fun times(other: BigDecimal): Dollar {
        return multiply(other)
    }
}
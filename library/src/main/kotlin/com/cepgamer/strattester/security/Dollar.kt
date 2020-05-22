package com.cepgamer.strattester.security

import java.math.BigDecimal
import java.math.RoundingMode

class Dollar(quantity: String) : BigDecimal(quantity) {
    constructor(quantity: Int) : this(quantity.toString())
    constructor(quantity: BigDecimal) : this(quantity.toPlainString())

    init {
        setScale(5, RoundingMode.HALF_UP)
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is BigDecimal -> compareTo(other) == 0
            else -> super.equals(other)
        }
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

    operator fun div(other: BigDecimal): Dollar {
        return Dollar(divide(other, 5, RoundingMode.HALF_UP))
    }
}
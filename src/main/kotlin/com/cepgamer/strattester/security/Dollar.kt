package com.cepgamer.strattester.security

import java.math.BigDecimal

data class Dollar(var quantity: BigDecimal) {
    infix operator fun plus(other: Dollar): Dollar {
        return Dollar(quantity + other.quantity)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Dollar) {
            return other.quantity == quantity
        }

        return false
    }

    override fun hashCode(): Int {
        return quantity.hashCode()
    }
}
package com.cepgamer.strattester.security

abstract class BaseSecurity(val symbol: String) {
    override fun toString(): String {
        return "Security with symbol $symbol"
    }
}

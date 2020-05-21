package com.cepgamer.strattester.security

data class Stock(val symbol: String) {
    override fun toString(): String {
        return "Security with symbol $symbol"
    }
}

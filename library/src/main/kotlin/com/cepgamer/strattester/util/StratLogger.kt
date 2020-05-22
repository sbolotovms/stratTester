package com.cepgamer.strattester.util

object StratLogger {
    var logger: ILogger? = null

    enum class Level {
        E,
        W,
        I,
        V
    }

    interface ILogger {
        fun log(message: CharSequence, level: Level)
    }

    fun i(message: CharSequence) {
        logger?.log(message, Level.I)
    }

    fun e(message: CharSequence) {
        logger?.log(message, Level.I)
    }
}
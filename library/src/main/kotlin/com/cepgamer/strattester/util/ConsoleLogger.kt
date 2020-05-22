package com.cepgamer.strattester.util

class ConsoleLogger(val loggingLevel: StratLogger.Level) : StratLogger.ILogger {
    override fun log(message: CharSequence, level: StratLogger.Level) {
        if (level < loggingLevel) {
            return
        }
        when (level) {
            StratLogger.Level.E,
            StratLogger.Level.W ->
                System.err.println(message)
            StratLogger.Level.I,
            StratLogger.Level.V ->
                println(message)
        }
    }
}
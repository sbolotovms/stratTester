package com.cepgamer.strattester

import com.cepgamer.strattester.data.YahooWebDownloader
import com.cepgamer.strattester.metric.SimpleGrowthMetric
import com.cepgamer.strattester.parser.YahooJSONParser
import com.cepgamer.strattester.runner.SavedDataStrategyRunner
import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.strategy.BlankStrategy
import com.cepgamer.strattester.strategy.MetricCutoffStrategy
import java.math.BigDecimal

object Main {
    fun moneyAvailable(): Dollar = Dollar(BigDecimal(10000))

    @JvmStatic
    fun main(args: Array<String>) {
        val symbol = "SPY"
        val security = Stock(symbol)
        val strat = BlankStrategy(security, moneyAvailable())
        val someStrat = MetricCutoffStrategy(SimpleGrowthMetric(), security, moneyAvailable(), BigDecimal(0))
        val json = YahooWebDownloader.getYahooHourlyData(
            symbol,
            YahooWebDownloader.feb1,
            YahooWebDownloader.mar1,
            "mar"
        )
        val rawData = YahooJSONParser(json).parse()
        val data = rawData.map {
            security as BaseSecurity to it
        }

        val strats = listOf(strat, someStrat)
        val runner = SavedDataStrategyRunner(strats, listOf(data))

        runner.run()

        println(strats)
    }
}
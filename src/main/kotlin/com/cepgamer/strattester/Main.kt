package com.cepgamer.strattester

import com.cepgamer.strattester.data.YahooWebDownloader
import com.cepgamer.strattester.parser.YahooJSONParser
import com.cepgamer.strattester.runner.SavedDataStrategyRunner
import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.strategy.BlankStrategy
import java.math.BigDecimal

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val strat = BlankStrategy(Dollar(BigDecimal(10000)))
        val symbol = "SPY"
        val json = YahooWebDownloader.getYahooHourlyData(
            symbol,
            YahooWebDownloader.feb1,
            YahooWebDownloader.mar1,
            "mar"
        )
        val security = Stock(symbol)
        val rawData = YahooJSONParser(json).parse()
        val data = rawData.map {
            security as BaseSecurity to it
        }

        val runner = SavedDataStrategyRunner(listOf(strat), listOf(data))

        runner.run()

        println(strat.moneyAvailable)
    }
}
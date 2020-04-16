package com.cepgamer.strattester

import com.cepgamer.strattester.data.YahooWebDownloader
import com.cepgamer.strattester.metric.InverseMetric
import com.cepgamer.strattester.metric.SimpleGrowthMetric
import com.cepgamer.strattester.metric.SwapSignalMetric
import com.cepgamer.strattester.metric.VolumeAmplifiedGrowth
import com.cepgamer.strattester.parser.YahooJSONParser
import com.cepgamer.strattester.runner.SavedDataStrategyRunner
import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.strategy.BaseStrategy
import com.cepgamer.strattester.strategy.BlankStrategy
import com.cepgamer.strattester.strategy.BuyStrategy
import com.cepgamer.strattester.strategy.MetricCutoffStrategy
import java.math.BigDecimal

object Main {
    val symbol = "SPY"
    val security = Stock(symbol)

    fun moneyAvailable(): Dollar = Dollar(BigDecimal(10000))

    @JvmStatic
    fun main(args: Array<String>) {
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
        val dailyData = PriceCandle.toDaily(data.map { it.second }).map {
            security to it
        }

        val strats = StrategyListGenerator(security).generate()

        val runner = SavedDataStrategyRunner(
            strats, listOf(dailyData)
        )

        runner.run()

        val weak = strats.filter { it.moneyAvailable.quantity <= BigDecimal(5_000) }

        println(strats)
        println("""

----------------------------------------------------------------
            Any successful strats: ${strats.find { it.moneyAvailable.quantity > moneyAvailable().quantity } != null}
            Successful strats: ${strats.filter { it.moneyAvailable.quantity > moneyAvailable().quantity }}
----------------------------------------------------------------
        """)
    }
}
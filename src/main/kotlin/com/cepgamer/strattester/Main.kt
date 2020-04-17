package com.cepgamer.strattester

import com.cepgamer.strattester.data.YahooWebDownloader
import com.cepgamer.strattester.parser.YahooJSONParser
import com.cepgamer.strattester.runner.SavedDataStrategyRunner
import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.strategy.BaseStrategy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.math.BigDecimal

object Main {
    val symbol = "SPY"
    val security = Stock(symbol)

    fun moneyAvailable(): Dollar = Dollar(BigDecimal(10000))

    fun stratsReport(strats: List<BaseStrategy>): String {
        return strats.toString() + """
----------------------------------------------------------------
            Total strats: ${strats.size}
            Any successful strats: ${strats.find { it.moneyAvailable.quantity > moneyAvailable().quantity } != null}
            Successful strats: ${strats.filter { it.moneyAvailable.quantity > moneyAvailable().quantity }}
----------------------------------------------------------------
        """
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val jsonFeb = YahooWebDownloader.getYahooHourlyData(
            symbol,
            YahooWebDownloader.feb1,
            YahooWebDownloader.mar1,
            "feb"
        )
        val jsonMar = YahooWebDownloader.getYahooHourlyData(
            symbol,
            YahooWebDownloader.mar1,
            YahooWebDownloader.apr1,
            "mar"
        )
        val rawData = YahooJSONParser(jsonFeb).parse() + YahooJSONParser(jsonMar).parse()
        val data = rawData.map {
            security as BaseSecurity to it
        }
        val dailyData = PriceCandle.toDaily(data.map { it.second }).map {
            security to it
        }

        val dailyStrats = StrategyListGenerator(security).generate()
        val strats = StrategyListGenerator(security).generate()

        val dailyRunner = SavedDataStrategyRunner(
            dailyStrats, listOf(dailyData)
        )
        val runner = SavedDataStrategyRunner(
            strats, listOf(data)
        )

        dailyRunner.run()
        runner.run()

        val dailyRes = stratsReport(dailyStrats.sortedByDescending { it.moneyAvailable.quantity })
        val res = stratsReport(strats.sortedByDescending { it.moneyAvailable.quantity })

        File("dailyRes.txt").apply {
            createNewFile()
            writeText(dailyRes)
        }

        File("res.txt").apply {
            createNewFile()
            writeText(res)
        }

        val weak = strats.filter { it.moneyAvailable.quantity <= BigDecimal(5_000) }
    }
}
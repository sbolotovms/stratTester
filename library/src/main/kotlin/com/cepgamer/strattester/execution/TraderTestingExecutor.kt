package com.cepgamer.strattester.execution

import com.cepgamer.strattester.runner.SavedDataTraderRunner
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.trader.BaseTrader
import com.cepgamer.strattester.util.StratLogger
import java.math.BigDecimal

class TraderTestingExecutor(
    symbol: String,
    rawData: List<PriceCandle>,
    private val traderGenerators: List<() -> BaseTrader>
) : BaseExecutor(symbol, rawData) {

    lateinit var reportCallback: ResultReportCallback

    fun performRun(
        data: List<Pair<Stock, PriceCandle>>,
        fileSuffix: String
    ) {
        val traders = traderGenerators.map { it() }
        val runner = SavedDataTraderRunner(
            traders, false, listOf(data)
        )
        StratLogger.i(
            """
            Trader count: ${traders.size}
            Total timestamp count: ${data.size}
        """
        )
        runner.run()
        val firstOpen = data.first().second.open
        val lastClose = data.last().second.close

        val priceDiffPercent = lastClose / firstOpen * BigDecimal(100)
        StratLogger.i(
            """
            Opened at: $firstOpen
            Closed at: $lastClose
            Gain/loss: $priceDiffPercent %
            
            Best strat gain: ${traders.maxBy { it.money }?.money}
            """
        )

        reportCallback.onResultTraders(
            traders,
            moneyAvailable.max(moneyAvailable * (data.last().second.close / data.first().second.close)),
            fileSuffix
        )
    }

    fun runDailyData(
        rawData: List<PriceCandle>
    ) {
        val dailyData = PriceCandle.toDaily(rawData).map {
            security to it
        }

        StratLogger.i("Running daily data")
        performRun(
            dailyData,
            "daily"
        )
    }

    fun runHourlyData(
        rawData: List<PriceCandle>
    ) {
        val data = rawData.map {
            security to it
        }
        StratLogger.i("Running hourly data")
        performRun(
            data,
            "hourly"
        )
    }

    override fun execute(resultReportCallback: ResultReportCallback) {
        reportCallback = resultReportCallback

        runDailyData(rawData)
        runHourlyData(rawData)
    }
}

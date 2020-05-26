package com.cepgamer.strattester.execution

import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.trader.BaseTrader

abstract class BaseExecutor(
    val symbol: String,
    val rawData: List<PriceCandle>
) {
    interface ResultReportCallback {
        fun onResultTraders(
            traders: List<BaseTrader>,
            successfulCriteria: Dollar,
            runName: String
        )
    }

    val security = Stock(symbol)

    val moneyAvailable: Dollar get() = Dollar(10000)

    /**
     * Note method in the callback can be called multiple times during execution, for different run results.
     */
    abstract fun execute(resultReportCallback: ResultReportCallback)
}
package com.cepgamer.strattester.runner

import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.trader.BaseTrader
import com.cepgamer.strattester.util.StratLogger

class SavedDataTraderRunner(
    traders: List<BaseTrader>,
    keepBad: Boolean = false,
    private val savedData: List<List<Pair<Stock, PriceCandle>>>
) : TraderRunner(traders, keepBad) {
    override fun run() {
        savedData.forEachIndexed { _: Int, next ->
            updateTraders(next)
        }
        StratLogger.i("Finished running traders")
    }
}
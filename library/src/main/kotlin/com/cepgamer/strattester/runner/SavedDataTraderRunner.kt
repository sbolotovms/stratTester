package com.cepgamer.strattester.runner

import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.trader.BaseTrader

class SavedDataTraderRunner(
    traders: List<BaseTrader>,
    private val savedData: List<List<Pair<Stock, PriceCandle>>>
) : TraderRunner(traders) {
    override fun run() {
        for (state in savedData) {
            updateTraders(state)
        }
    }
}
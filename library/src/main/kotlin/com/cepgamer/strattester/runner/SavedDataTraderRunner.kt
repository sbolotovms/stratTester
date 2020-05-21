package com.cepgamer.strattester.runner

import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.trader.BaseTrader

class SavedDataTraderRunner(
    traders: List<BaseTrader>,
    private val savedData: List<List<Pair<BaseSecurity, PriceCandle>>>
) : TraderRunner(traders) {
    override fun run() {
        for (state in savedData) {
            updateTraders(state)
        }
    }
}
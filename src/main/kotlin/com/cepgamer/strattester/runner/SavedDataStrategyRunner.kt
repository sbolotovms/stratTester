package com.cepgamer.strattester.runner

import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.strategy.BaseStrategy

class SavedDataStrategyRunner(
    strategies: List<BaseStrategy>,
    private val savedData: List<List<Pair<BaseSecurity, PriceCandle>>>
) : StrategyRunner(strategies) {
    override fun run() {
        for (state in savedData) {
            updateStrategies(state)
        }
    }
}
package com.cepgamer.strattester.execution

import com.cepgamer.strattester.generator.StrategyListGenerator
import com.cepgamer.strattester.generator.TraderGenerator
import java.time.YearMonth

class GeneratedStrategiesExecutor(
    symbol: String,
    startDate: YearMonth,
    endDate: YearMonth,
    haveCustom: Boolean = true,
    haveMetricCutoffs: Boolean = true,
    havePLCutoffs: Boolean = true,
    haveInverse: Boolean = true
) : BaseExecutor(symbol, startDate, endDate) {

    private val traderTestingExecutor: TraderTestingExecutor

    init {
        val strats = StrategyListGenerator(
            security,
            haveCustom = haveCustom,
            haveMetricCutoffs = haveMetricCutoffs,
            haveInverse = haveInverse
        ).generate()
        val traders = TraderGenerator(strats, havePLCutoffs = havePLCutoffs).generate().map { it() }

        traderTestingExecutor = TraderTestingExecutor(symbol, startDate, endDate, traders)
    }

    override fun execute() {
        traderTestingExecutor.execute()
    }
}
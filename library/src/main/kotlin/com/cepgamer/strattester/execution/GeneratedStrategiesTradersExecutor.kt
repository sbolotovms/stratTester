package com.cepgamer.strattester.execution

import com.cepgamer.strattester.generator.StrategyListGenerator
import com.cepgamer.strattester.generator.TraderGenerator
import com.cepgamer.strattester.security.PriceCandle
import java.time.YearMonth

open class GeneratedStrategiesTradersExecutor(
    symbol: String,
    rawData: List<PriceCandle>,
    startDate: YearMonth,
    endDate: YearMonth,
    haveCustom: Boolean = true,
    haveMetricCutoffs: Boolean = true,
    havePLCutoffs: Boolean = true,
    haveInverse: Boolean = true
) : BaseExecutor(symbol, rawData) {

    private val traderTestingExecutor: TraderTestingExecutor

    init {
        val strats = StrategyListGenerator(
            security,
            haveCustom = haveCustom,
            haveMetricCutoffs = haveMetricCutoffs,
            haveInverse = haveInverse
        ).generate()
        val traders = TraderGenerator(strats, havePLCutoffs = havePLCutoffs).generate()

        traderTestingExecutor = TraderTestingExecutor(symbol, rawData, traders)
    }

    override fun execute(resultReportCallback: ResultReportCallback) {
        traderTestingExecutor.execute(resultReportCallback)
    }
}
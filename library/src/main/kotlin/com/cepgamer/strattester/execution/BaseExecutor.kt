package com.cepgamer.strattester.execution

import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Stock
import com.cepgamer.strattester.trader.BaseTrader
import java.math.BigDecimal

abstract class BaseExecutor(
    val symbol: String,
    val rawData: List<PriceCandle>
) {
    val security = Stock(symbol)

    val moneyAvailable: Dollar get() = Dollar(10000)

    fun tradersReport(
        traders: List<BaseTrader>,
        reportAll: Boolean = false,
        successfulCriteria: BigDecimal = moneyAvailable
    ): String {
        val reportTop = 75
        return """${if (reportAll) traders.toString() else ""}
----------------------------------------------------------------
            Total Traders: ${traders.size}
            Any successful traders: ${traders.find { it.money > successfulCriteria } != null}
            Top $reportTop Successful traders: ${traders.filter { it.money > successfulCriteria }
            .sortedBy { it.money }.takeLast(reportTop)}
----------------------------------------------------------------
        """
    }

    abstract fun execute()
}
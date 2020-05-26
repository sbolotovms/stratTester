package com.cepgamer.strattester.report

import com.cepgamer.strattester.trader.BaseTrader
import java.math.BigDecimal

class TradersReport {

    fun tradersReport(
        traders: List<BaseTrader>,
        reportAll: Boolean = false,
        successfulCriteria: BigDecimal
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
}
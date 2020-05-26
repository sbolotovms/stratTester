package com.cepgamer.strattester.application

import com.cepgamer.strattester.execution.BaseExecutor
import com.cepgamer.strattester.report.TradersReport
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.trader.BaseTrader
import java.io.File
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class ResultReportCallback(
    val symbol: String,
    val startDate: YearMonth,
    val endDate: YearMonth
) : BaseExecutor.ResultReportCallback {

    val tradersReport = TradersReport()

    override fun onResultTraders(
        traders: List<BaseTrader>,
        successfulCriteria: Dollar,
        runName: String
    ) {
        val res = tradersReport.tradersReport(
            traders.filter { it.transactions.isNotEmpty() },
            successfulCriteria = successfulCriteria
        )

        val formatter = DateTimeFormatter.ofPattern("yy-MMM")
        val prefix = "${symbol}/${startDate.format(formatter)}_${endDate.format(formatter)}"
        val writeFile = { result: String ->
            { it: File ->
                it.apply {
                    parentFile.mkdirs()
                    createNewFile()
                    writeText(result)
                }
            }
        }
        File("$prefix/res_$runName.txt").let(writeFile(res))
    }
}
package com.cepgamer.strattester.data

import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class DataDownloadManager(
    val symbol: String,
    startDate: YearMonth, endDate: YearMonth
) {
    val months = mutableListOf<Pair<Long, String>>()

    init {
        val formatter = DateTimeFormatter.ofPattern("MMM")
        var dateI = startDate
        val dateN = endDate.plusMonths(1)

        while (dateI.isBefore(dateN)) {
            val date = Date.from(dateI.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
            months += date.time / 1000 to dateI.format(formatter)
            dateI = dateI.plusMonths(1)
        }
    }

    val yahooJsons: List<String>
        get() {
            val res = mutableListOf<String>()
            for (i in 0 until months.size - 1) {
                val date = months[i]
                res += YahooWebDownloader.getYahooHourlyData(
                    symbol,
                    date.first.toInt(),
                    months[i + 1].first.toInt(),
                    date.second
                )
            }

            return res
        }
}

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

        while (startDate.isBefore(endDate)) {
            val date = Date.from(startDate.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
            months += date.time / 1000 to startDate.format(formatter)
            startDate.plusMonths(1)
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

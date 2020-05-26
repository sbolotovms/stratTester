package com.cepgamer.strattester.execution

import com.cepgamer.strattester.data.DataDownloadManager
import com.cepgamer.strattester.parser.YahooJSONParser
import com.cepgamer.strattester.security.PriceCandle
import java.time.YearMonth

class YahooDataGeneratedStrategiesExecutor(
    symbol: String,
    startDate: YearMonth,
    endDate: YearMonth,
    haveCustom: Boolean = true,
    haveMetricCutoffs: Boolean = true,
    havePLCutoffs: Boolean = true,
    haveInverse: Boolean = true
) : GeneratedStrategiesTradersExecutor(
    symbol,
    downloadRawData(symbol, startDate, endDate),
    haveCustom,
    haveMetricCutoffs,
    havePLCutoffs, haveInverse
) {
    companion object {
        fun downloadRawData(symbol: String, startDate: YearMonth, endDate: YearMonth): List<PriceCandle> {
            val downloader = DataDownloadManager(symbol, startDate, endDate)
            val rawData = downloader.yahooJsons.map { YahooJSONParser(it).parse() }.reduce { acc, list -> acc + list }
            return rawData
        }
    }
}


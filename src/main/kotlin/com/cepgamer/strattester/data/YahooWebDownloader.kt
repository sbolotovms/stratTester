package com.cepgamer.strattester.data

import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class YahooWebDownloader(
    symbol: String,
    period1: Int,
    period2: Int,
    interval: String,
    preAndPostMarket: Boolean
) :
    HttpDownloader(
        URL(
            "https://query1.finance.yahoo.com/v8/finance/chart/$symbol?symbol=$symbol" +
                    "&period1=$period1&period2=$period2&interval=$interval&includePrePost=$preAndPostMarket"
        )
    ) {
    override fun setUpConnection(connection: HttpURLConnection) {
        connection.apply {
            addRequestProperty("authority", "query1.finance.yahoo.com")
        }
    }

    companion object {
        const val feb1 = 1580562000
        const val mar1 = 1583067600
        const val apr1 = 1585742400
        const val may1 = 1588334400

        const val dataFolder = "data/hourly/"

        private fun getFileName(symbol: String, month: String) = "$dataFolder$symbol/$month.json"

        private fun writeYahooHourlyData(symbol: String, time1: Int, time2: Int, month: String): String {
            val json = YahooWebDownloader(symbol, time1, time2, "1h", false).download()
            val file = File(getFileName(symbol, month))
            file.parentFile.mkdirs()
            file.createNewFile()
            file.writeText(json)

            return json
        }

        fun getYahooHourlyData(symbol: String, time1: Int, time2: Int, month: String): String =
            File(getFileName(symbol, month)).let { file ->
                return if (file.exists()) {
                    file.readText()
                } else {
                    writeYahooHourlyData(symbol, time1, time2, month)
                }
            }
    }
}
// curl 'https://query1.finance.yahoo.com/v8/finance/chart/SPY?symbol=SPY&period1=1584385200&period2=1584716400&interval=15m&includePrePost=true&events=div%7Csplit%7Cearn&lang=en-US&region=US&crumb=NfrcPyWoAQX&corsDomain=finance.yahoo.com' --compressed

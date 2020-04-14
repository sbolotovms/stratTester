package com.cepgamer.strattester.data

import java.net.HttpURLConnection
import java.net.URL

class YahooWebDownloader(symbol: String, period1: Int, period2: Int, interval: String, preAndPostMarket: Boolean) :
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
    // curl 'https://query1.finance.yahoo.com/v8/finance/chart/SPY?symbol=SPY&period1=1584385200&period2=1584716400&interval=15m&includePrePost=true&events=div%7Csplit%7Cearn&lang=en-US&region=US&crumb=NfrcPyWoAQX&corsDomain=finance.yahoo.com' --compressed
}
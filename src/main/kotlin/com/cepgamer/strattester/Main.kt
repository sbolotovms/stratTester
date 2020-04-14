package com.cepgamer.strattester

import com.cepgamer.strattester.data.YahooWebDownloader
import java.io.File

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val feb1 = 1580544000
        val mar1 = 1583049600
        val apr1 = 1585724400
        val may1 = 1588316400
        writeYahooHourlyData(feb1, mar1, "feb")
        writeYahooHourlyData(mar1, apr1, "mar")
        writeYahooHourlyData(apr1, may1, "apr")
    }

    private fun writeYahooHourlyData(time1: Int, time2: Int, month: String) {
        val json = YahooWebDownloader("SPY", time1, time2, "1h", false).download()
        val febFile = File("data/hourly/$month.json")
        febFile.parentFile.mkdirs()
        febFile.createNewFile()
        febFile.writeText(json)
    }
}
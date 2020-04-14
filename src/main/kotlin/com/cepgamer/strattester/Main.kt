package com.cepgamer.strattester

import com.cepgamer.strattester.data.YahooWebDownloader
import com.cepgamer.strattester.parser.YahooJSONParser
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.strategy.BlankStrategy
import java.math.BigDecimal

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val strat = BlankStrategy(Dollar(BigDecimal(10000)))
        val data = YahooJSONParser("${YahooWebDownloader.dataFolder}feb.json").parse()
    }
}
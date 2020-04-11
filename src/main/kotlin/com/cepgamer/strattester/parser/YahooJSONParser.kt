package com.cepgamer.strattester.parser

import com.cepgamer.strattester.security.PriceCandle
import kotlinx.serialization.json.Json
import java.io.File

class YahooJSONParser(filename: String) : BaseParser() {
    private val jsonFile = File(filename)

    override fun parse(): List<PriceCandle> {
        Json.parseJson(jsonFile.readText())

        return emptyList()
    }
}
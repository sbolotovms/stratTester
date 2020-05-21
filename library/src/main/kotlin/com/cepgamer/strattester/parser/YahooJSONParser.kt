package com.cepgamer.strattester.parser

import com.cepgamer.strattester.data.FileLoader
import com.cepgamer.strattester.security.PriceCandle
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import java.math.BigDecimal
import java.util.stream.IntStream
import kotlin.streams.toList

class YahooJSONParser(private val json: String?, filename: String? = null) : BaseParser() {
    private val jsonFile = filename?.let { FileLoader(filename) }

    override fun parse(): List<PriceCandle> {
        val json = Json.parseJson(json ?: jsonFile?.load() ?: throw IllegalArgumentException())

        val result = json.jsonObject.getObject("chart").getArray("result")[0].jsonObject
        val timestamps = result.getArray("timestamp").stream().mapToInt {
            it.primitive.int
        }

        val candles = result.getObject("indicators").getArray("quote")[0].jsonObject

        return parseJsonCandles(timestamps, candles)
    }

    private fun parseJsonCandles(timestamps: IntStream, candles: JsonObject): List<PriceCandle> {
        val timestamp = timestamps.toList()
        val bigDecimalConverter = { it: JsonElement ->
            if (it.primitive.content != "null") {
                BigDecimal(it.primitive.content)
            }
            else {
                BigDecimal.ZERO
            }
        }
        val zeroFilter = {it: BigDecimal ->
            it != BigDecimal.ZERO
        }
        val lows = candles.getArray("low").stream().map(bigDecimalConverter).filter(zeroFilter).toList()
        val volumes = candles.getArray("volume").stream().map(bigDecimalConverter).filter(zeroFilter).toList()
        val opens = candles.getArray("open").stream().map(bigDecimalConverter).filter(zeroFilter).toList()
        val highs = candles.getArray("high").stream().map(bigDecimalConverter).filter(zeroFilter).toList()
        val closes = candles.getArray("close").stream().map(bigDecimalConverter).filter(zeroFilter).toList()

        assert(
            timestamp.size == lows.size
                    && lows.size == volumes.size
                    && volumes.size == opens.size
                    && opens.size == highs.size
                    && highs.size == closes.size
        )

        val candlesList = ArrayList<PriceCandle>(timestamp.size)
        val range = timestamp[1] - timestamp[0]
        for (i in volumes.indices) {
            candlesList.add(
                PriceCandle(
                    opens[i],
                    closes[i],
                    lows[i],
                    highs[i],
                    volumes[i],
                    timestamp[i].toLong() * 1000L,
                    range,

                )
            )
        }

        return candlesList
    }
}
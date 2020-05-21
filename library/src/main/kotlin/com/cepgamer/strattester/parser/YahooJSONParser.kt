package com.cepgamer.strattester.parser

import com.cepgamer.strattester.data.FileLoader
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Stock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.content
import java.math.BigDecimal
import java.util.stream.IntStream
import kotlin.streams.toList

class YahooJSONParser(private val json: String?, filename: String? = null) : BaseParser() {
    private val jsonFile = filename?.let { FileLoader(filename) }

    override fun parse(): List<PriceCandle> {
        val json = Json.parseJson(json ?: jsonFile?.load() ?: throw IllegalArgumentException())

        val result = json.jsonObject.getObject("chart").getArray("result")[0].jsonObject
        val stock = Stock(result.getObject("meta").getPrimitive("symbol").content)
        val timestamps = result.getArray("timestamp").stream().mapToInt {
            it.primitive.int
        }

        val candles = result.getObject("indicators").getArray("quote")[0].jsonObject

        return parseJsonCandles(timestamps, candles,)
    }

    private fun parseJsonCandles(
        timestamps: IntStream,
        candles: JsonObject,
        stock: Stock
    ): List<PriceCandle> {
        val timestamp = timestamps.toList()
        val intConverter = { it: JsonElement ->
            it.primitive.int
        }
        val stringConverter = { it: JsonElement ->
            it.content
        }
        val nullFilter = { it: JsonElement ->
            it.content != "null"
        }
        val lows = candles.getArray("low").stream().filter(nullFilter).map(stringConverter).toList()
        val opens = candles.getArray("open").stream().filter(nullFilter).map(stringConverter).toList()
        val highs = candles.getArray("high").stream().filter(nullFilter).map(stringConverter).toList()
        val closes = candles.getArray("close").stream().filter(nullFilter).map(stringConverter).toList()
        val volumes = candles.getArray("volume").stream().filter(nullFilter).map(intConverter).toList()

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
                    stock
                )
            )
        }

        return candlesList
    }
}
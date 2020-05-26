package com.cepgamer.strattester.security

import java.util.stream.Collectors

class PriceCandleConverter {

    fun toDaily(candles: List<PriceCandle>): List<PriceCandle> {
        val listOfLists = candles.stream().collect(Collectors.groupingBy { candle: PriceCandle ->
            (candle.openTimestamp / 1000L - 21600) / (24 * 60 * 60)
        }).toList()
        val list = listOfLists.map {
            it.second.reduce { acc, priceCandle ->
                return@reduce PriceCandle(
                    acc.open,
                    priceCandle.open,
                    acc.low.min(priceCandle.low),
                    acc.high.max(priceCandle.high),
                    acc.volume + priceCandle.volume,
                    acc.openTimestamp,
                    acc.timespan + priceCandle.timespan,
                    acc.stock
                )
            }
        }.sortedBy { it.openTimestamp }

        return list
    }
}

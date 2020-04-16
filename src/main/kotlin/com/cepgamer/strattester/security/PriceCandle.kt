package com.cepgamer.strattester.security

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.stream.Collectors

data class PriceCandle(
    var open: BigDecimal,
    var close: BigDecimal,
    var low: BigDecimal,
    var high: BigDecimal,
    var volume: BigDecimal,
    val openTimestamp: Int,
    val timespan: Int
) {
    constructor(
        open: Int,
        close: Int,
        low: Int,
        high: Int,
        volume: Int,
        openTimestamp: Int,
        timespan: Int
    ) : this(
        BigDecimal(open),
        BigDecimal(close),
        BigDecimal(low),
        BigDecimal(high),
        BigDecimal(volume),
        openTimestamp,
        timespan
    )

    init {
        open = open.setScale(5, RoundingMode.HALF_UP)
        close = close.setScale(5, RoundingMode.HALF_UP)
        low = low.setScale(5, RoundingMode.HALF_UP)
        high = high.setScale(5, RoundingMode.HALF_UP)
        volume = volume.setScale(5, RoundingMode.HALF_UP)
    }

    companion object {
        fun toDaily(candles: List<PriceCandle>): List<PriceCandle> {
            val listOfLists = candles.stream().collect(Collectors.groupingBy { candle: PriceCandle ->
                candle.openTimestamp / (24 * 60 * 60)
            }).toList()
            val list = listOfLists.map {
                it.second.reduce { acc, priceCandle ->
                    PriceCandle(
                        acc.open,
                        priceCandle.open,
                        acc.low.min(priceCandle.low),
                        acc.high.max(priceCandle.high),
                        acc.volume + priceCandle.volume,
                        acc.openTimestamp,
                        acc.timespan + priceCandle.timespan
                    )
                }
            }

            return list
        }
    }
}
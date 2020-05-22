package com.cepgamer.strattester.security

import java.math.BigDecimal
import java.util.stream.Collectors

data class PriceCandle(
    val open: Dollar,
    val close: Dollar,
    val low: Dollar,
    val high: Dollar,
    var volume: BigDecimal,
    val openTimestamp: Long,
    val timespan: Int,
    val stock: Stock
) {
    constructor(
        open: Int,
        close: Int,
        low: Int,
        high: Int,
        volume: Int,
        openTimestamp: Int,
        timespan: Int,
        stock: Stock
    ) : this(
        Dollar(open),
        Dollar(close),
        Dollar(low),
        Dollar(high),
        BigDecimal(volume),
        openTimestamp.toLong(),
        timespan,
        stock
    )

    constructor(
        open: String,
        close: String,
        low: String,
        high: String,
        volume: Int,
        openTimestamp: Long,
        timespan: Int,
        stock: Stock
    ) : this(
        Dollar(open),
        Dollar(close),
        Dollar(low),
        Dollar(high),
        BigDecimal(volume),
        openTimestamp,
        timespan,
        stock
    )

    val sellPrice: Dollar
        get() = close

    val buyPrice: Dollar
        get() = close

    val openToCloseGrowth: BigDecimal
        get() {
            return if (high == low)
                BigDecimal.ZERO
            else
                (close - open) / (high - low)
        }

    companion object {
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
}
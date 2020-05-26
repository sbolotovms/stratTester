package com.cepgamer.strattester.security

import java.math.BigDecimal
import java.util.stream.Collectors

data class PriceCandle(
    val open: Dollar,
    val close: Dollar,
    val low: Dollar,
    val high: Dollar,
    val volume: BigDecimal,
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

    fun deepCopy(): PriceCandle {
        return PriceCandle(
            open = Dollar(open),
            close = Dollar(close),
            low = Dollar(low),
            high = Dollar(high),
            volume = BigDecimal(volume.toPlainString()),
            openTimestamp = openTimestamp,
            timespan = timespan,
            stock = stock
        )
    }

    val sellPrice: Dollar = close

    val buyPrice: Dollar = close

    val openToCloseGrowth: BigDecimal =
        if (high == low)
            BigDecimal.ZERO
        else
            (close - open) / (high - low)
}
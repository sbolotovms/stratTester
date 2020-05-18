package com.cepgamer.strattester

import com.cepgamer.strattester.security.PriceCandle

object TestConstants {
    val growthCandle = PriceCandle(
        1,
        2,
        1,
        2,
        5,
        100,
        60
    )
    val shrinkCandle = PriceCandle(
        2,
        1,
        1,
        2,
        5,
        100,
        60
    )
}
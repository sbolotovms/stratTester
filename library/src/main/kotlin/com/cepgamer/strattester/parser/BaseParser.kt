package com.cepgamer.strattester.parser

import com.cepgamer.strattester.security.PriceCandle

abstract class BaseParser {
    abstract fun parse(): List<PriceCandle>
}
package com.cepgamer.strattester.trader

import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.strategy.BaseStrategy

class StrategyTrader(val strategy: BaseStrategy, money: Dollar) : BaseTrader(money) {
}
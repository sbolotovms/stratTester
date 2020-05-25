package com.cepgamer.strattester.trader

import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.strategy.BaseStrategy
import java.math.BigDecimal
import kotlin.math.max

class ProfitLossLockTrader(
    strategy: BaseStrategy,
    moneyAvailable: Dollar,
    private val profitCutoffPercentage: BigDecimal = BigDecimal(5).setScale(5),
    private val lossCutoffPercentage: BigDecimal = BigDecimal(5).setScale(5),
    private val sinceLastSaleWait: Int = 5
) : StrategyTrader(strategy, moneyAvailable) {

    var sinceLastSaleCurrent: Int = 0
    override fun priceUpdate(priceCandle: PriceCandle) {
        val sellingPrice = priceCandle.sellPrice
        sinceLastSaleCurrent = max(0, sinceLastSaleCurrent - 1)
        if (sinceLastSaleCurrent > 0)
            return
        for (position in ArrayList(openPositions)) {
            val isProfit = position.purchasePrice < sellingPrice
            val difference = ((position.purchasePrice / sellingPrice) - BigDecimal(1).setScale(5)).abs() * BigDecimal(100)
            if (isProfit && difference >= profitCutoffPercentage) {
                closePosition(priceCandle, position)
                sinceLastSaleCurrent = sinceLastSaleWait
            } else if (!isProfit && difference >= lossCutoffPercentage) {
                closePosition(priceCandle, position)
                sinceLastSaleCurrent = sinceLastSaleWait
            }
        }

        if (sinceLastSaleCurrent == 0)
            super.priceUpdate(priceCandle)
    }

    override fun toString(): String {
        return super.toString() + """
                Profit cutoff: $profitCutoffPercentage
                Loss cutoff: $lossCutoffPercentage"""
    }

    companion object {
        /**
         * Generates (N + 1) * (M + 1) strategies from 0 to 1 with 1/N step for good signal and 1/M step for bad signal.
         */
        fun generateNbyMTraders(
            strategy: BaseStrategy,
            moneyAvailable: Dollar,
            profitLockPercentages: List<Int>,
            lossLockPercentages: List<Int>,
            sinceLastSaleCurrent: List<Int>
        ): List<() -> BaseTrader> {
            val list =
                profitLockPercentages.map { profit ->
                    lossLockPercentages.map { loss ->
                        sinceLastSaleCurrent.map { since ->
                            {
                                ProfitLossLockTrader(
                                    strategy,
                                    Dollar(moneyAvailable),
                                    BigDecimal(profit).setScale(5),
                                    BigDecimal(loss).setScale(5),
                                    since
                                )
                            }
                        }
                    }.reduce { list1: List<() -> ProfitLossLockTrader>, list2: List<() -> ProfitLossLockTrader> ->
                        list1 + list2
                    }
                }.reduce { list1: List<() -> ProfitLossLockTrader>, list2: List<() -> ProfitLossLockTrader> ->
                    list1 + list2
                }

            return list
        }
    }
}

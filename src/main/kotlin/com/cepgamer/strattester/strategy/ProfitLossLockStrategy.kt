package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.metric.BaseMetric
import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import java.math.BigDecimal

class ProfitLossLockStrategy(
    metric: BaseMetric,
    security: BaseSecurity,
    moneyAvailable: Dollar,
    val profitCutoffPercentage: BigDecimal = BigDecimal(5).setScale(5),
    val lossCutoffPercentage: BigDecimal = BigDecimal(5).setScale(5),
    goodSignalCutoff: BigDecimal = BigDecimal.ZERO,
    badSignalCutoff: BigDecimal = BigDecimal.ZERO
) : MetricCutoffStrategy(metric, security, moneyAvailable, goodSignalCutoff, badSignalCutoff) {
    var sinceLastSaleCurrent: Int = 0
    var sinceLastSaleWait: Int = 5

    override fun priceUpdate(priceCandle: PriceCandle): Action {
        val sellingPrice = priceCandle.low
        var didSell = false
        for (position in openPositions) {
            val isProfit = position.purchasePrice < sellingPrice
            val difference = ((position.purchasePrice / sellingPrice) - BigDecimal(1).setScale(5)).abs()
            if (isProfit && difference >= profitCutoffPercentage) {
                closePosition(priceCandle, position)
                sinceLastSaleCurrent = sinceLastSaleWait
                didSell = true
            } else if (!isProfit && difference >= lossCutoffPercentage) {
                closePosition(priceCandle, position)
                sinceLastSaleCurrent = sinceLastSaleWait
                didSell = true
            }
        }

        return when {
            didSell -> Action.SELL
            sinceLastSaleCurrent == 0 -> super.priceUpdate(priceCandle)
            else -> {
                sinceLastSaleCurrent--
                Action.HOLD
            }
        }
    }

    companion object {
        /**
         * Generates (N + 1) * (M + 1) strategies from 0 to 1 with 1/N step for good signal and 1/M step for bad signal.
         */
        fun generateNbyMStrategies(
            metric: BaseMetric,
            security: BaseSecurity,
            moneyAvailable: Dollar,
            profitLockPercentages: List<Int>,
            lossLockPercentages: List<Int>,
            n: Int,
            m: Int
        ): List<() -> BaseStrategy> {
            val list =
                profitLockPercentages.map { profit ->
                    lossLockPercentages.map { loss ->
                        (0..n).map { i ->
                            (0..m).map { j ->
                                {
                                    ProfitLossLockStrategy(
                                        metric,
                                        security,
                                        moneyAvailable.copy(),
                                        BigDecimal(profit).setScale(5),
                                        BigDecimal(loss).setScale(5),
                                        BigDecimal(i) / BigDecimal(n),
                                        BigDecimal(j) / BigDecimal(n)
                                    )
                                }
                            }
                        }.reduce { list1: List<() -> ProfitLossLockStrategy>, list2: List<() -> ProfitLossLockStrategy> ->
                            list1 + list2
                        }
                    }.reduce { list1: List<() -> ProfitLossLockStrategy>, list2: List<() -> ProfitLossLockStrategy> ->
                        list1 + list2
                    }
                }.reduce { list1: List<() -> ProfitLossLockStrategy>, list2: List<() -> ProfitLossLockStrategy> ->
                    list1 + list2
                }

            return list
        }
    }
}
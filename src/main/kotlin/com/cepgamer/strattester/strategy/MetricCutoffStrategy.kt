package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.metric.BaseMetric
import com.cepgamer.strattester.security.BaseSecurity
import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Transaction
import java.math.BigDecimal

class MetricCutoffStrategy(
    metric: BaseMetric,
    security: BaseSecurity,
    moneyAvailable: Dollar,
    val goodSignalCutoff: BigDecimal = BigDecimal.ZERO,
    val badSignalCutoff: BigDecimal = BigDecimal.ZERO
) : MetricStrategy(metric, security, moneyAvailable) {
    override fun priceUpdate(
        priceCandle: PriceCandle
    ) {
        metric.newData(priceCandle)

        if (metric.goodSignal >= goodSignalCutoff) {
            try {
                val transaction = Transaction.purchase(security, priceCandle, moneyAvailable)
                updateData(transaction)
            } catch (e: Transaction.TransactionFailedException) {
                println("Purchase failed: ${e.message}")
            }
        } else if (metric.badSignal >= badSignalCutoff) {
            openPosition?.let { open ->
                if (open.quantity >= BigDecimal.ZERO) {
                    val transaction = Transaction.sell(open, priceCandle, moneyAvailable)
                    updateData(transaction)
                }
            }
        }
    }

    companion object {
        /**
         * Generates N + 1 strategies from 0 to 1 with 1/N step.
         */
        fun generateNStrategies(
            metric: BaseMetric,
            security: BaseSecurity,
            moneyAvailable: Dollar,
            n: Int
        ): List<BaseStrategy> {
            return (0..n).map { i ->
                MetricCutoffStrategy(
                    metric,
                    security,
                    moneyAvailable.copy(),
                    BigDecimal(i) / BigDecimal(n),
                    BigDecimal(i) / BigDecimal(n)
                )
            }
        }
    }
}
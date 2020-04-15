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
        security: BaseSecurity,
        priceCandle: PriceCandle
    ) {
        metric.newData(priceCandle)

        if (metric.goodSignal >= goodSignalCutoff) {
            val transaction = Transaction.purchase(security, priceCandle, moneyAvailable)
            updateData(transaction)
        } else if (metric.badSignal >= badSignalCutoff) {
            openPosition?.let { open ->
                if (open.quantity >= BigDecimal.ZERO) {
                    val transaction = Transaction.sell(open, priceCandle, moneyAvailable)
                }
            }
        }
    }
}
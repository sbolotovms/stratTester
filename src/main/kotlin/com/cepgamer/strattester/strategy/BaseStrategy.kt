package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.security.*

abstract class BaseStrategy(
    val security: BaseSecurity,
    val moneyAvailable: Dollar
) {
    val transactions: MutableList<Transaction> = mutableListOf()
    val positions: MutableList<Position> = mutableListOf()

    var openPosition: Position? = null

    fun updateData(transaction: Transaction, position: Position) {
        updateData(transaction to position)
    }

    fun updateData(pair: Pair<Transaction, Position>) {
        transactions.add(pair.first)
        positions.add(pair.second)

        openPosition = if (pair.second.status == Position.Status.OPEN) {
            pair.second
        } else {
            null
        }
    }

    abstract fun priceUpdate(
        security: BaseSecurity,
        priceCandle: PriceCandle
    )

    fun closePosition(priceCandle: PriceCandle) {
        openPosition?.let { open ->
            val transaction = Transaction.sell(open, priceCandle, moneyAvailable)
            updateData(transaction)
        }
    }

    override fun toString(): String {
        return """
            Money available: $moneyAvailable
            Current position: $openPosition
            All positions: $positions
            All transactions: $transactions"""
    }
}
package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.security.*

abstract class BaseStrategy(
    val security: BaseSecurity,
    val moneyAvailable: Dollar
) {
    val transactions: MutableList<Transaction> = mutableListOf()
    val positions: MutableList<Position> = mutableListOf()

    val openPositions: MutableList<Position> = mutableListOf()

    fun updateData(transaction: Transaction, position: Position) {
        updateData(transaction to position)
    }

    fun updateData(pair: Pair<Transaction, Position>) {
        transactions.add(pair.first)
        positions.add(pair.second)

        if (pair.second.status == Position.Status.OPEN) {
            openPositions.add(pair.second)
        } else {
            openPositions.remove(pair.second)
        }
    }

    abstract fun priceUpdate(
        priceCandle: PriceCandle
    )

    fun closePosition(priceCandle: PriceCandle) {
        openPositions.forEach { open ->
            val transaction = Transaction.sell(open, priceCandle, moneyAvailable)
            updateData(transaction)
        }
    }

    override fun toString(): String {
        return """
            Type: ${this::class.simpleName}
            Money available: $moneyAvailable
            Current position: $openPositions
            All positions: $positions
            All transactions: $transactions"""
    }
}
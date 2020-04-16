package com.cepgamer.strattester.strategy

import com.cepgamer.strattester.security.*

abstract class BaseStrategy(
    val security: BaseSecurity,
    val moneyAvailable: Dollar
) {
    enum class Action {
        BUY,
        SELL,
        HOLD
    }

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
    ): Action

    fun purchaseStock(priceCandle: PriceCandle): Action {
        try {
            val transaction = Transaction.purchase(security, priceCandle, moneyAvailable)
            updateData(transaction)
            return Action.BUY
        } catch (e: Transaction.TransactionFailedException) {
            println("Purchase failed: ${e.message}")
        }
        return Action.HOLD
    }

    fun closePositions(priceCandle: PriceCandle): Action {
        if (openPositions.size >= 0) {
            ArrayList(openPositions).forEach { open ->
                val transaction = Transaction.sell(open, priceCandle, moneyAvailable)
                updateData(transaction)
            }
            return Action.SELL
        }
        return Action.HOLD
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
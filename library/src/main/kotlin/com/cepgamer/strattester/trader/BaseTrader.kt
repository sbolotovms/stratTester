package com.cepgamer.strattester.trader

import com.cepgamer.strattester.security.Dollar
import com.cepgamer.strattester.security.Position
import com.cepgamer.strattester.security.PriceCandle
import com.cepgamer.strattester.security.Transaction
import com.cepgamer.strattester.strategy.BaseStrategy
import com.cepgamer.strattester.util.StratLogger

abstract class BaseTrader(val money: Dollar) {

    val transactions: MutableList<Transaction> = mutableListOf()
    val positions: MutableList<Position> = mutableListOf()

    val openPositions: MutableList<Position> = mutableListOf()

    fun updateData(pair: Pair<Transaction, Position>) {
        transactions.add(pair.first)
        positions.add(pair.second)

        if (pair.second.status == Position.Status.OPEN) {
            openPositions.add(pair.second)
        } else {
            openPositions.remove(pair.second)
        }
    }

    fun updateData(transaction: Transaction, position: Position) {
        updateData(transaction to position)
    }

    fun purchaseStock(priceCandle: PriceCandle): BaseStrategy.Action {
        try {
            val transaction = Transaction.purchase(security, priceCandle, money)
            updateData(transaction)
            return BaseStrategy.Action.BUY
        } catch (e: Transaction.TransactionFailedException) {
            StratLogger.i("Purchase failed: ${e.message}")
        }
        return BaseStrategy.Action.HOLD
    }

    fun closePositions(priceCandle: PriceCandle): BaseStrategy.Action {
        if (openPositions.size >= 0) {
            ArrayList(openPositions).forEach { open ->
                closePosition(priceCandle, open)
            }
            return BaseStrategy.Action.SELL
        }
        return BaseStrategy.Action.HOLD
    }

    fun closePosition(priceCandle: PriceCandle, position: Position) {
        val transaction = Transaction.sell(position, priceCandle, money)
        updateData(transaction)
    }

    override fun toString(): String {
        return """
            Type: ${this::class.simpleName}
            Money available: $money
            Current position: $openPositions
            All positions: $positions
            All transactions: $transactions"""
    }
}
package com.cepgamer.strattester.trader

import com.cepgamer.strattester.security.*
import com.cepgamer.strattester.strategy.BaseStrategy
import com.cepgamer.strattester.util.StratLogger
import java.util.concurrent.atomic.AtomicReference

abstract class BaseTrader(money: Dollar) {

    private val _moneyInternal = AtomicReference(money)
    var money: Dollar
        get() = _moneyInternal.get()

        set(value) {
            _moneyInternal.set(value)
        }

    val startMoney = Dollar(money)

    val transactions: MutableList<Transaction> = mutableListOf()
    val positions: MutableSet<Position> = mutableSetOf()

    val openPositions: MutableList<Position> = mutableListOf()

    abstract fun priceUpdate(priceCandle: PriceCandle)

    fun updateData(pair: Pair<Transaction, Position>) {
        transactions.add(pair.first)

        if (pair.second.status == Position.Status.OPEN) {
            openPositions.add(pair.second)
            positions.add(pair.second)
        } else {
            openPositions.remove(pair.second)
        }
    }

    fun updateData(transaction: Transaction, position: Position) {
        updateData(transaction to position)
    }

    fun purchaseStock(
        priceCandle: PriceCandle,
        security: Stock,
        moneyToUse: Dollar
    ): BaseStrategy.Action {
        try {
            synchronized(this) {
                if (money < moneyToUse)
                    throw TradingException("Insufficient funds")
                val transaction = Transaction.purchase(security, priceCandle, this, moneyToUse)
                updateData(transaction)
                return BaseStrategy.Action.BUY
            }
        } catch (e: TradingException) {
            StratLogger.e("Purchase failed: ${e.message}")
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
        synchronized(this) {
            val transaction = Transaction.sell(position, priceCandle, this)
            updateData(transaction)
        }
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
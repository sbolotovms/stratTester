package com.cepgamer.strattester.application

import com.cepgamer.strattester.execution.YahooDataGeneratedStrategiesExecutor
import java.time.Month
import java.time.YearMonth

class StrategyTestingApplication(
    val args: Array<String>
) {
    private var `continue` = true
    var haveCustom: Boolean = false
    var haveMetricCutoffs: Boolean = false
    var havePLCutoffs: Boolean = false
    var haveInverse: Boolean = false
    var months = YearMonth.of(2020, Month.JANUARY) to YearMonth.of(2020, Month.MAY)
    var stock = "TVIX"

    fun parseArgs() {
        for (arg in args) {
            when {
                arg == "-c" ->
                    haveCustom = true
                arg == "-mc" ->
                    haveMetricCutoffs = true
                arg == "-pl" ->
                    havePLCutoffs = true
                arg == "-i" ->
                    haveInverse = true
                arg.startsWith("-m") -> {
                    val pairAsList = arg
                        .substring(2)
                        .trim().trim { it == '=' }
                        .split("-")
                        .map {
                            val mmyy = it.split('/')
                            YearMonth.of(mmyy[1].toInt(), mmyy[0].toInt())
                        }
                    months = pairAsList[0] to pairAsList[1]
                }
                arg.startsWith("-s") -> {
                    stock = arg
                        .substring(2)
                        .trim().trim { it == '=' }
                }
                arg == "-h" -> {
                    println(
                        """
                            Parameters:
                            -m - time in the following format:
                              mm/yy-mm/yy
                            Where first date represents start date (inclusive), second represents end date (non-inclusive).
                            NOTE no more than a year time frame for now.
                            
                            -s stock - stock on which to run tests.
                            
                            Traders:
                            -c - include custom traders.
                            -mc - include metric cutoff strategy traders.
                            -pl - include profit/loss lock traders.
                            -i - include inverse traders.

                            -h - show this message.
                        """
                    )
                    `continue` = false
                }
            }
        }
    }

    fun execute() {
        YahooDataGeneratedStrategiesExecutor(
            stock,
            months.first,
            months.second,
            haveCustom,
            haveMetricCutoffs,
            havePLCutoffs,
            haveInverse
        ).execute(ResultReportCallback(stock, months.first, months.second))

    }

    fun main() {

        parseArgs()

        if (`continue`) {
            execute()
        }
    }
}
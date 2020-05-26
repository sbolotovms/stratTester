package com.cepgamer.strattester

import com.cepgamer.strattester.executionScenarios.StrategyTestingScenario
import java.time.Month
import java.time.YearMonth

object Main {
    var haveCustom: Boolean = false
    var haveMetricCutoffs: Boolean = false
    var havePLCutoffs: Boolean = false
    var haveInverse: Boolean = false

    @JvmStatic
    fun main(args: Array<String>) {
        var months = YearMonth.of(2020, Month.JANUARY) to YearMonth.of(2020, Month.MAY)
        var stock = "TVIX"
        for (arg in args) {
            when {
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
                arg == "-c" ->
                    haveCustom = true
                arg == "-mc" ->
                    haveMetricCutoffs = true
                arg == "-pl" ->
                    haveCustom = true
                arg == "-i" ->
                    haveInverse = true
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
                    return
                }
            }
        }



        StrategyTestingScenario(
            stock,
            months.first,
            months.second,
            haveCustom,
            haveMetricCutoffs,
            havePLCutoffs,
            haveInverse
        ).runStrategyTests()
    }
}

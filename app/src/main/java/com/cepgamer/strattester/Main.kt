package com.cepgamer.strattester

import com.cepgamer.strattester.executionScenarios.StrategyTestingScenario
import java.time.Month
import java.time.YearMonth

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        var months = YearMonth.of(2020, Month.JANUARY) to YearMonth.of(2020, Month.MAY)
        var stock = "TVIX"
        for (arg in args) {
            if (arg.startsWith("-m")) {
                val pairAsList = arg
                    .substring(2)
                    .trim().trim { it == '=' }
                    .split("-")
                    .map {
                        val mmyy = it.split('/')
                        YearMonth.of(mmyy[1].toInt(), mmyy[0].toInt())
                    }
                months = pairAsList[0] to pairAsList[1]
            } else if (arg.startsWith("-s")) {
                stock = arg
                    .substring(2)
                    .trim().trim { it == '=' }
            } else if (arg.startsWith("-h")) {
                println(
                    """
                    Parameters:
                    -m - time in the following format:
                      mm/yy-mm/yy
                    Where first date represents start date (inclusive), second represents end date (non-inclusive).
                    NOTE no more than a year time frame for now.
                    
                    -s stock - stock on which to run tests.
                    
                    -h - show this message.
                """
                )
                return
            }
        }



        StrategyTestingScenario(stock, months.first, months.second).runStrategyTests()
    }
}

package com.cepgamer.strattester

import com.cepgamer.strattester.executionScenarios.StrategyTestingScenario

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        var months = setOf("apr")
        var stock = "TVIX"
        for (arg in args) {
            if (arg.startsWith("-m")) {
                months = arg
                    .substring(2)
                    .trim().trim { it == '=' }
                    .split(";")
                    .toSet()
            } else if (arg.startsWith("-s")) {
                stock = arg
                    .substring(2)
                    .trim().trim { it == '=' }
            }
        }

        StrategyTestingScenario(months, stock).runStrategyTests()
    }
}

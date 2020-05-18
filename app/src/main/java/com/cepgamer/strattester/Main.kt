package com.cepgamer.strattester

import com.cepgamer.strattester.executionScenarios.StrategyTestingScenario

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        StrategyTestingScenario(setOf("apr"), "TVIX").runStrategyTests()
    }
}

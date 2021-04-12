package com.ninjacontrol.verifikation.engine

import com.ninjacontrol.verifikation.scenario
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.platform.engine.discovery.DiscoverySelectors.selectClass
import org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage
import org.junit.platform.testkit.engine.EngineTestKit

internal class VerifikationEngineTest {
    @Test
    fun `it should discover tests`() {

        EngineTestKit
            .engine(VerifikationEngine())
            .selectors(
                selectPackage("com.ninjacontrol.verifikation.engine"),
                selectClass(TestEngineScenarios::class.java))
            .execute()
            .testEvents()
            .assertStatistics { stats -> stats.started(2).finished(2) }
    }
}

class TestEngineScenarios {

    @TestScenario
    fun test1() = scenario {
        step {}
    }
}

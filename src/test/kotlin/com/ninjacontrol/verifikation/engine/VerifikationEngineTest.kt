package com.ninjacontrol.verifikation.engine

import com.ninjacontrol.verifikation.scenario
import org.junit.jupiter.api.Test
import org.junit.platform.engine.discovery.DiscoverySelectors.selectClass
import org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage
import org.junit.platform.testkit.engine.EngineTestKit

internal class VerifikationEngineTest {
    @Test
    fun `it should discover tests and execute`() {
        EngineTestKit
            .engine(VerifikationEngine())
            .selectors(
                selectPackage("com.ninjacontrol.verifikation.engine"),
                selectClass(TestEngineScenarios::class.java)
            )
            .execute()
            .testEvents().debug()
            .assertStatistics { stats ->
                stats.started(1)
                    .finished(1)
                    .succeeded(1)
            }
    }
}

class TestEngineScenarios {

    @TestScenario
    fun `it should work`() = scenario {
        name = "test scenario 1"
        step {}
    }
}

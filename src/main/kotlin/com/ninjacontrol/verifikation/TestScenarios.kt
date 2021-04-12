package com.ninjacontrol.verifikation

import com.ninjacontrol.verifikation.engine.TestScenario

class TestScenarios {

    @TestScenario
    fun `it should do stuff`() = scenario {

        group {
            step {}
            step {}
            step {}
        }
        group {
            step {}
        }
    }

    @TestScenario
    val s = scenario {
    }
}

val s = scenario { }

object someSuite {
    val s2 = scenario {
    }
}

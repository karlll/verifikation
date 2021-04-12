package com.ninjacontrol.verifikation

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ScenarioTest {
    @Test
    fun `it should be possible to define a scenario containing steps in a default group`() {
        val s = scenario {
            step {}
            step {}
            step {}
        }
        assertEquals(3, s.groups[0].steps.size)
        assertEquals(1, s.groups.size)
    }

    @Test
    fun `it should be possible to define a scenario containing steps in a single group`() {
        val s = scenario {
            group {
                step {}
                step {}
                step {}
            }
        }
        assertEquals(3, s.groups[0].steps.size)
        assertEquals(1, s.groups.size)
    }

    @Test
    fun `it should be possible to define a scenario with an empty group`() {
        val s = scenario {
            group {
            }
        }
        assertEquals(0, s.groups[0].steps.size)
        assertEquals(1, s.groups.size)
    }

    @Test
    fun `it should NOT be possible to define a scenario with steps outside a group`() {

        assertThrows(PreconditionException::class.java) {
            scenario {
                group {}
                step {}
            }
        }

        assertThrows(PreconditionException::class.java) {
            scenario {
                step {}
                group {}
            }
        }
    }

    @Test
    fun `it should be possible to define a scenario with an many groups containing steps`() {
        val s = scenario {
            group {
                step {}
            }
            group {
                step {}
                step {}
            }
            group {}
            group {
                step {}
                step {}
                step {}
            }
        }
        assertEquals(1, s.groups[0].steps.size)
        assertEquals(2, s.groups[1].steps.size)
        assertEquals(0, s.groups[2].steps.size)
        assertEquals(3, s.groups[3].steps.size)
        assertEquals(4, s.groups.size)
    }
}

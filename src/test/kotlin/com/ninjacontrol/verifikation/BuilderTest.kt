package com.ninjacontrol.verifikation

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BuilderTest {

    @Test
    fun `it should verify that mandatory parameters are set`() {
        val testBuilder = TestBuilder()
        testBuilder.mandatoryProperty = null
        assertThrows(PreconditionException::class.java) {
            testBuilder.build()
        }
    }
    @Test
    fun `it should build an instance of the class provided as type parameter`() {
        val testBuilder = TestBuilder()
        assertEquals(TestClass::class, testBuilder.build()::class)
    }
}

class TestClass

class TestBuilder : Builder<TestClass> {
    @Mandatory
    var mandatoryProperty: String? = "foo"
    override fun buildInstance(): TestClass = TestClass()
}

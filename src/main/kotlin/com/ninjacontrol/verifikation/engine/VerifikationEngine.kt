package com.ninjacontrol.verifikation.engine

import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.classgraph.MethodInfo
import org.junit.platform.engine.EngineDiscoveryRequest
import org.junit.platform.engine.ExecutionRequest
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestEngine
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.discovery.PackageSelector
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor
import org.junit.platform.engine.support.descriptor.EngineDescriptor

class VerifikationEngine : TestEngine {

    companion object {
        const val engineId = "verifikation-engine"
        const val testScenarioAnnotationName = "com.ninjacontrol.verifikation.engine.TestScenario"
    }

    val executor: Executor = ScenarioExecutor()

    override fun getId(): String = engineId

    override fun discover(
        discoveryRequest: EngineDiscoveryRequest?,
        uniqueId: UniqueId?
    ): TestDescriptor {
        if (discoveryRequest != null && uniqueId != null) {
            val packageSelector = discoveryRequest.getSelectorsByType(PackageSelector::class.java)

            val classGraphScan = ClassGraph().run {
                enableAllInfo()
                packageSelector.forEach {
                    acceptPackages(it.packageName)
                }
                scan()
            }

            val engineDescriptor = EngineDescriptor(uniqueId, "Verifikation")

            val testClasses =
                classGraphScan.getClassesWithMethodAnnotation(testScenarioAnnotationName)
            testClasses.forEach { testClass ->
                testClass.methodInfo.forEach { testMethod ->
                    if (testMethod.annotationInfo.containsName(testScenarioAnnotationName)) {
                        engineDescriptor.addChild(
                            VerifikationTestDescriptor(
                                testClass,
                                testMethod,
                                UniqueId.forEngine(id),
                                "${testMethod.className}#${testMethod.name}"
                            )
                        )
                    }
                }
            }

            return engineDescriptor
        } else {
            throw IllegalAccessException("Bad invocation.")
        }
    }

    override fun execute(request: ExecutionRequest?) {
        request?.let { req ->
            executor.execute(req)
        }
    }
}

interface Executor {
    fun execute(request: ExecutionRequest)
}

@Target(allowedTargets = [AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION])
annotation class TestScenario

class VerifikationTestDescriptor(
    val classInfo: ClassInfo,
    val methodInfo: MethodInfo,
    engineId: UniqueId,
    displayName: String
) : AbstractTestDescriptor(engineId.append("verifikation-test", displayName), displayName) {
    override fun getType(): TestDescriptor.Type = TestDescriptor.Type.TEST
}

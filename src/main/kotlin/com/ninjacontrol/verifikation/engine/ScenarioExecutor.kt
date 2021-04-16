package com.ninjacontrol.verifikation.engine

import com.ninjacontrol.verifikation.PreconditionException
import com.ninjacontrol.verifikation.Scenario
import mu.KotlinLogging
import org.junit.platform.engine.ExecutionRequest
import org.junit.platform.engine.TestExecutionResult

private val log = KotlinLogging.logger {}

class ScenarioExecutor : Executor {

    override fun execute(request: ExecutionRequest) {
        var totalResult: TestExecutionResult? = null
        val rootTestDescriptor = request.rootTestDescriptor
        request.engineExecutionListener.executionStarted(rootTestDescriptor)
        log.info("Execution started: ${rootTestDescriptor.uniqueId}")
        request.rootTestDescriptor.children.forEach { testDescriptor ->
            if (testDescriptor is VerifikationTestDescriptor) {
                var result: TestExecutionResult = TestExecutionResult.successful()
                try {
                    request.engineExecutionListener.executionStarted(testDescriptor)
                    result = executeTest(testDescriptor)
                } catch (ex: Throwable) {
                    log.error("Failure: $ex")
                    result = TestExecutionResult.failed(ex)
                } finally {
                    request.engineExecutionListener.executionFinished(testDescriptor, result)
                }
                totalResult = aggregateResult(totalResult, result)
            }
        }
        request.engineExecutionListener.executionFinished(
            rootTestDescriptor,
            totalResult
        )
        log.info("Execution finished: ${rootTestDescriptor.uniqueId}")
    }

    private fun aggregateResult(
        current: TestExecutionResult?,
        new: TestExecutionResult
    ): TestExecutionResult {
        if (current == null) return new
        return when (current.status) {
            TestExecutionResult.Status.ABORTED, TestExecutionResult.Status.FAILED -> current
            else -> {
                if (current.status != new.status) {
                    log.debug("Update aggregate state ${current.status} -> ${new.status}")
                }
                new
            }
        }
    }

    private fun executeTest(testDescriptor: VerifikationTestDescriptor): TestExecutionResult {

        val testClassInstance =
            testDescriptor.classInfo.loadClass().getDeclaredConstructor().newInstance()
        val getScenario = testDescriptor.methodInfo.loadClassAndGetMethod()

        if (getScenario.returnType != Scenario::class.java) {
            throw PreconditionException("Method ${getScenario.declaringClass.name}#${getScenario.name} has invalid return type, should return Scenario.")
        }

        val scenario = getScenario.invoke(testClassInstance) as Scenario

        scenario.name?.let {
            log.info("Loaded scenario $it.")
        } ?: log.info("Loaded scenario.")

        return TestExecutionResult.successful()
    }
}

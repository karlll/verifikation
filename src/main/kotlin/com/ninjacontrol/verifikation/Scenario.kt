package com.ninjacontrol.verifikation

open class Scenario {
    var name: String? = null
    var groups = ArrayList<Group>()
}

fun scenario(init: ScenarioBuilder.() -> Unit): Scenario {
    val scenario = ScenarioBuilder()
    scenario.init()
    return scenario.build()
}

class ScenarioBuilder : Scenario(), Builder<Scenario> {

    var currentGroup: Int = 0

    fun group(init: GroupBuilder.() -> Unit) {
        if (groups.size >= 1 && groups[0].defaultGroup) {
            throw PreconditionException("Please add step to specific group")
        }
        val group = GroupBuilder()
        group.init()
        groups.add(group.build())
        currentGroup++
    }

    fun step(init: StepBuilder.() -> Unit) {
        val stepBuilder = StepBuilder()
        stepBuilder.init()
        when (groups.size) { // Only allow steps in default group if no other groups are defined
            0 -> groups.add(GroupBuilder(true))
            1 -> if (!groups[0].defaultGroup) { throw PreconditionException("Please add step to specific group") }
            else -> throw PreconditionException("Please add step to specific group")
        }
        groups[0].steps.add(stepBuilder.build())
    }

    override fun buildInstance(): Scenario {
        val scenario = Scenario()
        scenario.let {
            it.name = name
            it.groups = groups
        }
        return scenario
    }
}

open class Group {
    var description: String? = null
    var steps = ArrayList<Step>()
    open val defaultGroup: Boolean = false
}

class GroupBuilder(override val defaultGroup: Boolean = false) : Group(), Builder<Group> {
    fun step(init: StepBuilder.() -> Unit) {
        val stepBuilder = StepBuilder()
        stepBuilder.init()
        steps.add(stepBuilder.build())
    }

    override fun buildInstance(): Group {
        val group = Group()
        group.let {
            it.description = description
            it.steps = steps
        }
        return group
    }
}

open class Step {
    var description: String? = null
}

class StepBuilder : Step(), Builder<Step> {
    override fun buildInstance(): Step {
        val step = Step()
        step.description = description
        return step
    }
}

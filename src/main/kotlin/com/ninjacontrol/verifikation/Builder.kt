package com.ninjacontrol.verifikation

import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation

@Target(allowedTargets = [AnnotationTarget.FIELD, AnnotationTarget.PROPERTY])
annotation class Mandatory

interface Builder<T> {

    fun verifyMandatory(instance: Builder<T>) {
        instance.javaClass.kotlin.declaredMemberProperties.filter {
            it.hasAnnotation<Mandatory>() && it.returnType.isMarkedNullable
        }.forEach {
            if (it.get(instance) == null) {
                throw PreconditionException("$it is mandatory and cannot be null.")
            }
        }
    }

    fun build(): T {
        verifyMandatory(this)
        return buildInstance()
    }

    fun buildInstance(): T
}

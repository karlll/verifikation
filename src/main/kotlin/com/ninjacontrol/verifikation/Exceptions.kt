package com.ninjacontrol.verifikation

sealed class BaseException(message: String) : Throwable(message)
class PreconditionException(message: String) : BaseException(message)

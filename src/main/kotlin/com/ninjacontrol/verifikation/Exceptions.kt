package com.ninjacontrol.verifikation

sealed class BaseException(message: String) : Exception(message)
class PreconditionException(message: String) : BaseException(message)

package com.climat.library.commandParser.exception

import com.climat.library.domain.ref.ParamDefinition

internal class ParameterException(val arg: ParamDefinition, message: String) : Error(message)

package com.climat.library.commandParser.exception

import com.climat.library.domain.ref.ParamDefinition

internal class ParameterMissingException(val arg: ParamDefinition) : Error()

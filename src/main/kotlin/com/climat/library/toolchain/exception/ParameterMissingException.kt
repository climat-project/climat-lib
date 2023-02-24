package com.climat.library.toolchain.exception

import com.climat.library.domain.ref.ParamDefinition

class ParameterMissingException(val arg: ParamDefinition) : Error()

package com.climat.library.domain.toolchain

import com.climat.library.domain.SourceTraceable
import com.climat.library.domain.action.Action
import com.climat.library.domain.ref.Constant
import com.climat.library.domain.ref.ParamDefinition
import com.climat.library.domain.ref.PredefinedParamDefinition
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
abstract class Toolchain : SourceTraceable() {
    abstract val name: String
    abstract val description: String
    abstract val parameters: Array<ParamDefinition>
    abstract val action: Action
    abstract val children: Array<DescendantToolchain>
    abstract val constants: Array<Constant>
    abstract val allowUnmatched: Boolean
    abstract val aliases: Array<Alias>
    abstract val predefinedParameters: Array<PredefinedParamDefinition>
}

package com.climat.library.domain.toolchain

import com.climat.library.domain.action.Action
import com.climat.library.domain.ref.Constant
import com.climat.library.domain.ref.ParamDefinition
import org.antlr.v4.kotlinruntime.misc.Interval
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
abstract class Toolchain {
    abstract val name: String
    abstract val description: String
    abstract val parameters: Array<ParamDefinition>
    abstract val action: Action
    abstract val children: Array<DescendantToolchain>
    abstract val constants: Array<Constant>
    abstract val allowUnmatched: Boolean

    internal abstract val sourceMap: ToolchainSourceMap
}

abstract class ToolchainSourceMap {
    abstract val name: Interval
    abstract val allowUnmatched: Interval?
}

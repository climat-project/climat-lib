package com.climat.library.domain.toolchain

import com.climat.library.domain.action.Action
import com.climat.library.domain.ref.Constant
import com.climat.library.domain.ref.ParamDefinition
import org.antlr.v4.kotlinruntime.ast.Position
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class RootToolchain internal constructor(
    val sourceCode: String,
    override val name: String,
    override val description: String,
    override val parameters: Array<ParamDefinition>,
    override val action: Action,
    override val children: Array<DescendantToolchain>,
    override val constants: Array<Constant>,
    override val allowUnmatched: Boolean,
    val resources: Array<String>,
    override val sourceMap: Position?
) : Toolchain() {
    override val aliases: Array<Alias>
        get() = emptyArray()
}

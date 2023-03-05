package com.climat.library.domain.toolchain

import com.climat.library.domain.action.Action
import com.climat.library.domain.ref.Constant
import com.climat.library.domain.ref.ParamDefinition
import com.climat.library.domain.ref.PredefinedParamDefinition
import org.antlr.v4.kotlinruntime.ast.Position
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class RootToolchain internal constructor(
    val sourceCode: String,
    override val name: String,
    override val description: String,
    override val parameters: Array<ParamDefinition>,
    override val action: Action,
    override val children: Array<DescendantToolchain>,
    override val constants: Array<Constant>,
    override val allowUnmatched: Boolean,
    val resources: Array<String>,
    override val sourceMap: Position?,
    override val aliases: Array<Alias>,
    override val predefinedParameters: Array<PredefinedParamDefinition>
) : Toolchain()

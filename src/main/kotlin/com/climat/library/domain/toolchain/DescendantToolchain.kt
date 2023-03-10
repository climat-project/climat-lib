package com.climat.library.domain.toolchain

import com.climat.library.domain.action.Action
import com.climat.library.domain.ref.Constant
import com.climat.library.domain.ref.ParamDefinition
import com.climat.library.domain.ref.PredefinedParamDefinition
import com.climat.library.utils.emptyString
import com.climat.library.utils.noopAction
import org.antlr.v4.kotlinruntime.ast.Position
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class DescendantToolchain internal constructor(
    override val name: String,
    override val description: String = emptyString(),
    override val parameters: Array<ParamDefinition> = emptyArray(),
    override val action: Action = noopAction(),
    override val children: Array<DescendantToolchain> = emptyArray(),
    override val constants: Array<Constant> = emptyArray(),
    override val allowUnmatched: Boolean,
    override val sourceMap: Position,
    override val aliases: Array<Alias>,
    override val predefinedParameters: Array<PredefinedParamDefinition>,
) : Toolchain()

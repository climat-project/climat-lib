package com.climat.library.domain.toolchain

import com.climat.library.domain.action.Action
import com.climat.library.domain.ref.Constant
import com.climat.library.domain.ref.ParamDefinition
import com.climat.library.emptyString
import com.climat.library.noopAction
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class DescendantToolchain internal constructor(
    override val name: String,
    override val description: String = emptyString(),
    override val parameters: Array<ParamDefinition> = emptyArray(),
    override val parameterDefaults: Map<String, String> = emptyMap(),
    override val action: Action = noopAction(),
    override val children: Array<DescendantToolchain> = emptyArray(),
    override val constants: Array<Constant> = emptyArray(),
    val aliases: Array<String> = emptyArray(),
) : Toolchain()

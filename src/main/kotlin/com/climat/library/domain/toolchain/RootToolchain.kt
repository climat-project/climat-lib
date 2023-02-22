package com.climat.library.domain.toolchain

import com.climat.library.domain.action.Action
import com.climat.library.domain.ref.Constant
import com.climat.library.domain.ref.ParamDefinition
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class RootToolchain internal constructor(
    override val name: String,
    override val description: String,
    override val parameters: Array<ParamDefinition>,
    override val action: Action,
    override val children: Array<DescendantToolchain>,
    override val constants: Array<Constant>,
    override val allowUnmatched: Boolean,
    val resources: Array<String>
) : Toolchain()

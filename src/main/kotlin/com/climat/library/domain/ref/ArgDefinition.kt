package com.climat.library.domain.ref

import org.antlr.v4.kotlinruntime.ast.Position
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class ArgDefinition internal constructor(
    override val sourceMap: Position?,
    override val shorthand: String?,
    override val description: String,
    override val name: String,
    override val optional: Boolean,
    override val default: String?,
) : ParamDefinition()

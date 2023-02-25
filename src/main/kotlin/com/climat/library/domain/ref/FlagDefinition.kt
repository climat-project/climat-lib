package com.climat.library.domain.ref

import org.antlr.v4.kotlinruntime.ast.Position
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class FlagDefinition internal constructor(
    override val sourceMap: Position?,
    override val shorthand: String?,
    override val description: String,
    override val name: String,
) : ParamDefinition() {
    override val optional: Boolean
        get() = true
    override val default: String?
        get() = null
}

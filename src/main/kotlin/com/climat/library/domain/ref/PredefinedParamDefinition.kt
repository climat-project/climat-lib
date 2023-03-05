package com.climat.library.domain.ref

import org.antlr.v4.kotlinruntime.ast.Position
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class PredefinedParamDefinition internal constructor(
    override val sourceMap: Position?,
    override val name: String,
) : Ref()

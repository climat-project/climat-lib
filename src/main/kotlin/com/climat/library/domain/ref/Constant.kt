package com.climat.library.domain.ref

import com.climat.library.domain.action.template.Template
import org.antlr.v4.kotlinruntime.ast.Position
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class Constant internal constructor(
    override val name: String,
    internal val value: Template,
    override val sourceMap: Position?,
    val isBoolean: Boolean
) : Ref()

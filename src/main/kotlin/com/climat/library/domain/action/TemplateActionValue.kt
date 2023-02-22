package com.climat.library.domain.action

import com.climat.library.domain.action.template.Template
import org.antlr.v4.kotlinruntime.ast.Position
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class TemplateActionValue internal constructor(
    internal val template: Template,
    override val sourceMap: Position?
) : ActionValueBase<String>()

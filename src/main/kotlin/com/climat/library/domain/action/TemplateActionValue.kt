package com.climat.library.domain.action

import com.climat.library.parser.template.Template
import org.antlr.v4.kotlinruntime.misc.Interval
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class TemplateActionValue internal constructor(
    internal val template: Template,
    override val sourceMap: Interval
) : ActionValueBase<String>()

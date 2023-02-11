package com.climat.library.domain.action

import com.climat.library.parser.template.Template
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class TemplateActionValue internal constructor(internal val template: Template) : ActionValueBase<String>()

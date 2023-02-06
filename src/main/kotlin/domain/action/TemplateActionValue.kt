package domain.action

import parser.template.Template
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class TemplateActionValue(val template: Template) : ActionValueBase<String>()

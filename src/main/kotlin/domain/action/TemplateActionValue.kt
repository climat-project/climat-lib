package domain.action

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class TemplateActionValue(val template: String) : ActionValueBase<String>()

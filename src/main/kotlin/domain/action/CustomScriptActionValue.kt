package domain.action

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class CustomScriptActionValue(
    val name: String?,
    val customScript: String
) : ActionValueBase<Map<String, String>>()

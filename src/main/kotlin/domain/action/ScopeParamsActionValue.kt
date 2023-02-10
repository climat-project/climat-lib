package domain.action

import jsExportable.JsExportableMap
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class ScopeParamsActionValue internal constructor() : ActionValueBase<Map<String, String>>() {
    val valueForJs: JsExportableMap<String, String>? = value?.let(::JsExportableMap)
}

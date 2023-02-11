package com.climat.library.domain.action

import com.climat.library.jsExportable.JsExportableMap
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class CustomScriptActionValue internal constructor(
    val name: String?,
    val customScript: String
) : ActionValueBase<Map<String, String>>() {
    val valueForJs: JsExportableMap<String, String>?
        get() = value?.let(::JsExportableMap)
}

package com.climat.library.domain.action

import com.climat.library.jsExportable.JsExportableMap
import org.antlr.v4.kotlinruntime.ast.Position
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class CustomScriptActionValue internal constructor(
    val name: String?,
    val customScript: String,
    override val sourceMap: Position?
) : ActionValueBase<Map<String, Any>>() {
    val valueForJs: JsExportableMap<String, Any>?
        get() = value?.let(::JsExportableMap)
}

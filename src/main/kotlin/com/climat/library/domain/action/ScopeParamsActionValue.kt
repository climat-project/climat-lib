package com.climat.library.domain.action

import com.climat.library.jsExportable.JsExportableMap
import org.antlr.v4.kotlinruntime.ast.Position
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class ScopeParamsActionValue internal constructor() : ActionValueBase<Map<String, String>>() {
    val valueForJs: JsExportableMap<String, String>? = value?.let(::JsExportableMap)
    override val sourceMap: Position?
        get() = null
}

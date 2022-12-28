package domain

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class Parameter(
    val name: String,
    val optional: Boolean,
    val shorthand: String?,
    val type: Toolchain.Type,
    val description: String?
)

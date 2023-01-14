package domain

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class ParamDefinition(
    val name: String,
    val optional: Boolean,
    val shorthand: String?,
    val type: Type,
    val description: String,
    val default: String?
) {
    enum class Type {
        Flag,
        Arg
    }
}
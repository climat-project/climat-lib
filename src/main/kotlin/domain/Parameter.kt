package domain

@OptIn(ExperimentalJsExport::class)
@JsExport
data class Parameter(
    val name: String,
    val optional: Boolean,
    val shorthand: String? = null,
    val type: Toolchain.Type,
    val description: String? = null
)

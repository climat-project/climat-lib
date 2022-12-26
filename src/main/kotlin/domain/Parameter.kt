package domain

data class Parameter(
    val name: String,
    val optional: Boolean,
    val shorthand: String?,
    val type: Toolchain.Type,
    val description: String?
)

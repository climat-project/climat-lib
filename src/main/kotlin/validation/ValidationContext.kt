package validation

import domain.toolchain.ToolchainBase

internal data class ValidationContext(
    val toolchain: ToolchainBase,
    val pathToRoot: List<ToolchainBase>
)

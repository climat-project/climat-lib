package validation

import domain.toolchain.Toolchain

internal data class ValidationContext(
    val toolchain: Toolchain,
    val pathToRoot: List<Toolchain>
)

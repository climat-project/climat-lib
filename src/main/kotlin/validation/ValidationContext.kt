package validation

import Toolchain

internal data class ValidationContext(
    val toolchain: Toolchain,
    val pathToRoot: List<Toolchain>
)

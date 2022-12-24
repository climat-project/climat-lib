package validation

import Toolchain

data class ValidationContext(
    val toolchain: Toolchain,
    val pathToRoot: List<Toolchain>
)

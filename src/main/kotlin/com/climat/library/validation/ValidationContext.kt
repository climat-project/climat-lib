package com.climat.library.validation

import com.climat.library.domain.toolchain.Toolchain

internal data class ValidationContext(
    val toolchain: Toolchain,
    val pathToRoot: List<Toolchain>
)

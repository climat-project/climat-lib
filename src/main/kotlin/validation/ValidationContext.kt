package validation

import Toolchain

data class ValidationContext(
    val scopeParams: Map<String, List<Toolchain.Parameter>>,
    val toolchain: Toolchain
)

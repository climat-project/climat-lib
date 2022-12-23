package validation

import Toolchain

data class ValidationContext(
    val scopeParams: Map<String, List<Toolchain.Parameter>>,
    val regexMatches: Sequence<MatchResult>,
    val toolchain: Toolchain
)

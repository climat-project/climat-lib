package domain

import domain.ref.Ref
import domain.toolchain.DescendantToolchain
import domain.toolchain.Toolchain

internal val Toolchain.refs: List<Ref>
    get() = this.constants.toList() +
        this.parameters.toList()

internal val DescendantToolchain.eachAlias: List<DescendantToolchain>
    get() = listOf(this) + this.aliases.map { this.copy(name = it) }

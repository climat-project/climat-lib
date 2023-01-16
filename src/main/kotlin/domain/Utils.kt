package domain

import domain.ref.Ref
import domain.toolchain.Toolchain
import domain.toolchain.ToolchainBase

internal val ToolchainBase.refs: List<Ref>
    get() = this.constants.toList() +
        this.parameters.toList()

internal val Toolchain.eachAlias: List<Toolchain>
    get() = listOf(this) + this.aliases.map { this.copy(name = it) }

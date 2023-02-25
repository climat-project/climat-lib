package com.climat.library.domain

import com.climat.library.domain.ref.Ref
import com.climat.library.domain.toolchain.DescendantToolchain
import com.climat.library.domain.toolchain.Toolchain

internal val Toolchain.refs: List<Ref>
    get() = constants.toList() +
        parameters.toList() +
        predefinedParameters.toList()

internal val DescendantToolchain.eachAlias: List<DescendantToolchain>
    get() = listOf(this) + this.aliases.map { this.copy(name = it.name) }

internal val Toolchain.isLeaf: Boolean
    get() = children.isEmpty()

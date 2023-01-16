package domain

internal val Toolchain.refs: List<Ref>
    get() = this.constants.toList() +
        this.parameters.toList()

internal val Toolchain.eachAlias: List<Toolchain>
    get() = listOf(this) + this.aliases.map { this.copy(name = it) }

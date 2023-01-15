package domain

internal val Toolchain.refs: List<Ref>
    get() = this.constants.toList() +
        this.parameters.toList()

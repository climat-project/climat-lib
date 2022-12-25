import kotlinx.cli.ArgType

internal fun toolchainParameterTypeToCliArgType(it: Toolchain.Type) = when (it) {
    Toolchain.Type.Arg -> ArgType.String
    Toolchain.Type.Flag -> ArgType.Boolean
}

internal fun <T> not(predicate: (T) -> Boolean): (T) -> Boolean = { it: T -> !predicate(it) }

internal fun emptyString(): String = ""

// TODO support multiplatform?
internal fun newLine(): String = "\n"

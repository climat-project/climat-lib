import kotlinx.cli.ArgType

internal fun toolchainParameterTypeToCliArgType(it: Toolchain.Type) = when (it) {
    Toolchain.Type.string -> ArgType.String
    Toolchain.Type.bool -> ArgType.Boolean
    Toolchain.Type.double -> ArgType.Double
    Toolchain.Type.int -> ArgType.Int
}

internal fun <T> not(predicate: (T) -> Boolean): (T) -> Boolean = { it: T -> !predicate(it) }

internal fun emptyString(): String = ""

// TODO support multiplatform?
internal fun newLine(): String = "\n"

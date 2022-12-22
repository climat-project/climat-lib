import kotlinx.cli.ArgType

fun toolchainParameterTypeToCliArgType(it: Toolchain.Type) = when (it) {
    Toolchain.Type.string -> ArgType.String
    Toolchain.Type.bool -> ArgType.Boolean
    Toolchain.Type.double -> ArgType.Double
    Toolchain.Type.int -> ArgType.Int
}
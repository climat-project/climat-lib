import domain.Toolchain
import kotlinx.cli.ArgType
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

internal fun toolchainParameterTypeToCliArgType(it: Toolchain.Type): ArgType<*> = when (it) {
    Toolchain.Type.Arg -> ArgType.String
    Toolchain.Type.Flag -> ArgType.Boolean
}

internal fun <T> not(predicate: (T) -> Boolean): (T) -> Boolean = { it: T -> !predicate(it) }

internal fun emptyString(): String = ""

// TODO support multiplatform?
internal fun newLine(): String = "\n"

internal val JsonElement.isJsonObject: Boolean
    get() = this as? JsonObject != null

internal val JsonElement.isString: Boolean
    get() = (this as? JsonPrimitive)?.isString == true
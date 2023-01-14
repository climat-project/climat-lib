
import domain.Referenceable
import kotlinx.cli.ArgType
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

internal fun toolchainParameterTypeToCliArgType(it: Referenceable.Type): ArgType<*> = when (it) {
    Referenceable.Type.Arg -> ArgType.String
    Referenceable.Type.Flag -> ArgType.Boolean
}

internal fun <T> not(predicate: (T) -> Boolean): (T) -> Boolean = { it: T -> !predicate(it) }

internal fun emptyString(): String = ""

// TODO support multiplatform?
internal fun newLine(): String = "\n"

internal val JsonElement.isJsonObject: Boolean
    get() = this as? JsonObject != null

internal val JsonElement.isString: Boolean
    get() = (this is JsonPrimitive) && this.isString

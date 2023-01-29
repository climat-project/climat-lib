
import domain.IAction
import domain.ref.Ref
import kotlinx.cli.ArgType

internal fun toolchainParameterTypeToCliArgType(it: Ref.Type): ArgType<*> = when (it) {
    Ref.Type.Arg -> ArgType.String
    Ref.Type.Flag -> ArgType.Boolean
}

internal fun <T> not(predicate: (T) -> Boolean): (T) -> Boolean = { it: T -> !predicate(it) }

internal fun emptyString(): String = ""

// TODO support multiplatform?
internal fun newLine(): String = "\n"

fun <K, V> Map<K, V?>.filterNotNullValues(): Map<K, V> =
    mapNotNull { (key, value) -> value?.let { key to it } }.toMap()

internal fun noopAction(): IAction = object : IAction {
    override val template: String
        get() = emptyString()
}

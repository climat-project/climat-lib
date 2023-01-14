package domain

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
// TODO: find better name
abstract class Referenceable {
    abstract val name: String
    abstract val type: Type

    enum class Type {
        Flag,
        Arg
    }
}
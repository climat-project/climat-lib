package domain.ref

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
abstract class Ref {
    abstract val name: String
    abstract val type: Type

    enum class Type {
        Flag,
        Arg
    }
}

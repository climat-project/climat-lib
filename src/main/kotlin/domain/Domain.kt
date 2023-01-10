@file:OptIn(ExperimentalJsExport::class)

package domain

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
interface IAction {
    val template: String
}

@JsExport
data class ParamDefinition(
    val name: String,
    val optional: Boolean,
    val shorthand: String?,
    val type: Type,
    val description: String,
    val default: String?
) {
    enum class Type {
        Flag,
        Arg
    }
}

@JsExport
data class Toolchain(
    val name: String,
    val description: String,
    val parameters: Array<ParamDefinition>,
    val action: IAction,
    val children: Array<Toolchain>
)

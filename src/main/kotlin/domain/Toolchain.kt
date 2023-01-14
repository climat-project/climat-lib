package domain

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class Toolchain(
    val name: String,
    val description: String,
    val parameters: Array<ParamDefinition>,
    val parameterDefaults: Map<String, String>,
    val action: IAction,
    val children: Array<Toolchain>
)
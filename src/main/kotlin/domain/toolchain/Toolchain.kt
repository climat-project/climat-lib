package domain.toolchain

import domain.action.Action
import domain.ref.Constant
import domain.ref.ParamDefinition
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
abstract class Toolchain {
    abstract val name: String
    abstract val description: String
    abstract val parameters: Array<ParamDefinition>
    abstract val parameterDefaults: Map<String, String>
    abstract val action: Action
    abstract val children: Array<DescendantToolchain>
    abstract val constants: Array<Constant>
}

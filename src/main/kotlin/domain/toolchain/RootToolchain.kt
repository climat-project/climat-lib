package domain.toolchain

import domain.IAction
import domain.ref.Constant
import domain.ref.ParamDefinition
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class RootToolchain(
    override val name: String,
    override val description: String,
    override val parameters: Array<ParamDefinition>,
    override val parameterDefaults: Map<String, String>,
    override val action: IAction,
    override val children: Array<DescendantToolchain>,
    override val constants: Array<Constant>,
    val resources: Array<String>
) : Toolchain()
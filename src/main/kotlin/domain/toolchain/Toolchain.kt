package domain.toolchain

import domain.IAction
import domain.ref.Constant
import domain.ref.ParamDefinition
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

abstract class ToolchainBase {
    abstract val name: String
    abstract val description: String
    abstract val parameters: Array<ParamDefinition>
    abstract val parameterDefaults: Map<String, String>
    abstract val action: IAction
    abstract val children: Array<Toolchain>
    abstract val constants: Array<Constant>
}

@OptIn(ExperimentalJsExport::class)
@JsExport
data class RootToolchain(
    override val name: String,
    override val description: String,
    override val parameters: Array<ParamDefinition>,
    override val parameterDefaults: Map<String, String>,
    override val action: IAction,
    override val children: Array<Toolchain>,
    override val constants: Array<Constant>,
    val resources: Array<String>
) : ToolchainBase()

@OptIn(ExperimentalJsExport::class)
@JsExport
data class Toolchain(
    override val name: String,
    val aliases: Array<String>,
    override val description: String,
    override val parameters: Array<ParamDefinition>,
    override val parameterDefaults: Map<String, String>,
    override val action: IAction,
    override val children: Array<Toolchain>,
    override val constants: Array<Constant>,
) : ToolchainBase()

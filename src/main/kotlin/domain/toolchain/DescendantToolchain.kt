package domain.toolchain

import domain.IAction
import domain.ref.Constant
import domain.ref.ParamDefinition
import emptyString
import noopAction
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class DescendantToolchain(
    override val name: String,
    override val description: String = emptyString(),
    override val parameters: Array<ParamDefinition> = emptyArray(),
    override val parameterDefaults: Map<String, String> = emptyMap(),
    override val action: IAction = noopAction(),
    override val children: Array<DescendantToolchain> = emptyArray(),
    override val constants: Array<Constant> = emptyArray(),
    val aliases: Array<String> = emptyArray(),
) : Toolchain()

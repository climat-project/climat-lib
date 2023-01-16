package domain

import domain.dto.RootToolchainDto
import domain.dto.ToolchainDto
import domain.dto.ToolchainDtoBase
import domain.ref.Constant
import domain.ref.ParamDefinition
import domain.ref.Ref
import domain.toolchain.RootToolchain
import domain.toolchain.Toolchain
import emptyString
import isJsonObject
import isString
import kotlinx.cli.ParsingException
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonPrimitive

private val parameterRegex = Regex("^(req|opt):(flag|arg):(\\w+)(:\\w)?(?:: *(.*))?\$")

internal fun convert(toolchain: RootToolchainDto): RootToolchain =
    RootToolchain(
        name = toolchain.name,
        description = toolchain.description,
        parameters = getParameters(toolchain),
        parameterDefaults = toolchain.paramDefaults,
        action = getAction(toolchain),
        children = getChildren(toolchain),
        constants = getConstants(toolchain),
        resources = toolchain.resources
    )

internal fun convert(toolchain: ToolchainDto): Toolchain =
    Toolchain(
        name = toolchain.name,
        aliases = toolchain.aliases,
        description = toolchain.description,
        parameters = getParameters(toolchain),
        parameterDefaults = toolchain.paramDefaults,
        action = getAction(toolchain),
        children = getChildren(toolchain),
        constants = getConstants(toolchain)
    )

private fun convertFromParamDefinitionString(paramDefinitionString: String): ParamDefinition {
    val match = parameterRegex.find(paramDefinitionString)
    requireNotNull(match) { "parameters item does not match pattern $parameterRegex - $paramDefinitionString" }

    val name = match.groupValues[3] // TODO give names to regex groups
    return ParamDefinition(
        name = name,
        optional = when (match.groupValues[1]) {
            "req" -> false
            "opt" -> true
            else -> throw Exception()
        },
        shorthand = match.groups[4]?.value,
        type = when (match.groupValues[2]) {
            "flag" -> Ref.Type.Flag
            "arg" -> Ref.Type.Arg
            else -> throw Exception()
        },
        description = match.groups[5]?.value ?: emptyString(),
    )
}

private fun adHocIAction(template: String): IAction =
    object : IAction {
        override val template: String
            get() = template
    }

private fun getParameters(toolchain: ToolchainDtoBase) = toolchain.parameters.map {
    convertFromParamDefinitionString(it)
}.toTypedArray()

private fun getConstants(toolchain: ToolchainDtoBase) = toolchain.constants.map { (name, value) ->
    val type =
        if (value.booleanOrNull != null)
            Ref.Type.Flag
        else if (value.isString)
            Ref.Type.Arg
        else
            throw ParsingException("Constant value must be either a string or a boolean")

    Constant(name, type, (value.booleanOrNull ?: value.content).toString())
}.toTypedArray()

private fun getChildren(toolchain: ToolchainDtoBase) = toolchain.children.map(::convert).toTypedArray()

private fun getAction(toolchain: ToolchainDtoBase) = toolchain.action?.let {
    if (it.isString) {
        adHocIAction(it.jsonPrimitive.content)
    } else if (it.isJsonObject) {
        throw Exception("Not implemented yet!")
    } else {
        throw ParsingException("action must be a string")
    }
} ?: adHocIAction(emptyString())


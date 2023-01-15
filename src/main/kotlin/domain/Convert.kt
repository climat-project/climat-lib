package domain

import ToolchainDto
import emptyString
import isJsonObject
import isString
import kotlinx.cli.ParsingException
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonPrimitive

private val parameterRegex = Regex("^(req|opt):(flag|arg):(\\w+)(:\\w)?(?:: *(.*))?\$")

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

internal fun convert(toolchain: ToolchainDto): Toolchain =
    Toolchain(
        name = toolchain.name,
        description = toolchain.description ?: emptyString(),
        parameters = toolchain.parameters.orEmpty().map {
            convertFromParamDefinitionString(it)
        }.toTypedArray(),
        parameterDefaults = toolchain.paramDefaults.orEmpty(),
        action = toolchain.action?.let {
            if (it.isString) {
                adHocIAction(it.jsonPrimitive.content)
            } else if (it.isJsonObject) {
                throw Exception("Not implemented yet!")
            } else {
                throw ParsingException("action must be a string")
            }
        } ?: adHocIAction(emptyString()),
        children = toolchain.children.orEmpty().map(::convert).toTypedArray(),
        constants = toolchain.constants.orEmpty().map { (name, value) ->
            val type =
                if (value.booleanOrNull != null)
                    Ref.Type.Flag
                else if (value.isString)
                    Ref.Type.Arg
                else
                    throw ParsingException("Constant value must be either a string or a boolean")

            Constant(name, type, (value.booleanOrNull ?: value.content).toString())
        }.toTypedArray()
    )

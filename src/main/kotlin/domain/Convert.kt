package domain

import ToolchainDto
import emptyString
import isJsonObject
import isString
import kotlinx.cli.ParsingException
import kotlinx.serialization.json.jsonPrimitive

private val parameterRegex = Regex("^(req|opt):(flag|arg):(\\w+)(:\\w)?(?:: *(.*))?\$")

private fun convertFromParamDefinitionString(paramDefinitionString: String, paramDefaults: Map<String, String>?): ParamDefinition {
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
            "flag" -> ParamDefinition.Type.Flag
            "arg" -> ParamDefinition.Type.Arg
            else -> throw Exception()
        },
        description = match.groups[5]?.value ?: emptyString(),
        default = paramDefaults?.get(name)
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
            convertFromParamDefinitionString(it, toolchain.paramDefaults)
        }.toTypedArray(),
        action = toolchain.action?.let {
            if (it.isString) {
                adHocIAction(it.jsonPrimitive.content)
            } else if (it.isJsonObject) {
                throw Exception("Not implemented yet!")
            } else {
                throw ParsingException("action must be a string")
            }
        } ?: adHocIAction(emptyString()),
        children = toolchain.children.orEmpty().map(::convert).toTypedArray()
    )

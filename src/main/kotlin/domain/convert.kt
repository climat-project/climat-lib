package domain

import ToolchainDto
import emptyString

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

fun convert(toolchain: ToolchainDto): Toolchain =
    Toolchain(
        name = toolchain.name,
        description = toolchain.description ?: emptyString(),
        parameters = toolchain.parameters.orEmpty().map {
            convertFromParamDefinitionString(it, toolchain.paramDefaults)
        }.toTypedArray(),
        action = object : IAction { // TODO proper implementation
            override val template: String
                get() = "abcd"
        },
        children = toolchain.children.orEmpty().map(::convert).toTypedArray()
    )
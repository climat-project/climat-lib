package template

import domain.ParameterWithValue
import domain.Toolchain
import emptyString
import not

internal data class ParamReference(
    val paramName: String,
    val mapTarget: String?,
    val range: IntRange
)

private val actionRe = Regex("\\\$\\(([\\w.]+)(?::([^ ()]+))?\\)")

internal fun MatchResult.isMapping() = this.groups[2] != null

internal fun MatchResult.getParamName() = this.groupValues[1]

internal fun MatchResult.getMappedFlag() = this.groupValues[2]

internal fun getParamReferences(template: String): Sequence<ParamReference> =
    actionRe.findAll(template)
        .map { match ->
            val flagMapTarget = if (match.isMapping()) {
                match.groupValues[2]
            } else {
                null
            }
            ParamReference(match.getParamName(), flagMapTarget, match.range)
        }

internal fun getActualCommand(
    template: String,
    paramValues: Map<String, ParameterWithValue>
): String {
    val missingValues = getParamReferences(template)
        .map { it.paramName }
        .distinct()
        .filter(not(paramValues::contains))
        .toList()
    if (missingValues.any()) {
        throw Exception(
            "Cannot compile command. Missing parameters: ${
            missingValues.joinToString(", ")
            }"
        )
    }

    return actionRe.replace(template) { match ->
        val value = paramValues[match.getParamName()]!!
        if (match.isMapping()) {
            if (value.definition.type == Toolchain.Type.Flag) {
                if (value.value.toBoolean()) {
                    match.getMappedFlag()
                } else {
                    emptyString()
                }
            } else {
                if (value.value != emptyString()) {
                    "${match.getMappedFlag()}='${value.value}'"
                } else {
                    emptyString()
                }
            }
        } else {
            value.value
        }
    }
}

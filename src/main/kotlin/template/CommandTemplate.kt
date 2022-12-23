package template

import emptyString
import not

data class ParamReference(
    val paramName: String,
    val flagMapTarget: String?,
    val range: IntRange
)

private val actionRe = Regex("\\\$\\(([\\w.]+)(?::([^ ()]+))?\\)")

fun MatchResult.isFlagMapping() = this.groups[2] != null

fun MatchResult.getParamName() = this.groupValues[1]

fun MatchResult.getMappedFlag() = this.groupValues[2]

fun getParamReferences(template: String): Sequence<ParamReference> =
    actionRe.findAll(template)
        .map { match ->
            val flagMapTarget = if (match.isFlagMapping()) {
                match.groupValues[2]
            } else {
                null
            }
            ParamReference(match.getParamName(), flagMapTarget, match.range)
        }

fun getActualCommand(template: String, paramValues: Map<String, String>): String {
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
        val value = paramValues[match.getParamName()]
        if (match.isFlagMapping()) {
            if (value.toBoolean()) {
                match.getMappedFlag()
            } else {
                emptyString()
            }
        } else {
            value!!
        }
    }
}

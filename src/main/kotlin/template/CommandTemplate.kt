package template

import domain.IAction
import domain.ParamDefinition
import leftovers.ParameterWithValue
import emptyString
import not

internal data class ParamReference(
    val paramName: String,
    val mapTarget: String?,
    val isFlipped: Boolean,
    val range: IntRange
)

private val actionRe = Regex("\\\$\\((!)?([\\w.]+)(?::([^ ()]+))?\\)")

internal fun MatchResult.isMapping() = this.groups[3] != null

internal fun MatchResult.isFlipped() = this.groups[1] != null

internal fun MatchResult.getParamName() = this.groupValues[2]

internal fun MatchResult.getMappedFlag() = this.groupValues[3]

internal fun getParamReferences(template: String): Sequence<ParamReference> =
    actionRe.findAll(template)
        .map { match ->
            val flagMapTarget = if (match.isMapping()) {
                match.getParamName()
            } else {
                null
            }
            ParamReference(
                match.getParamName(),
                flagMapTarget,
                match.isFlipped(),
                match.range
            )
        }

internal fun getActualCommand(
    action: IAction,
    paramValues: Map<String, ParameterWithValue>
): String {
    val missingValues = getParamReferences(action.template)
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

    return actionRe.replace(action.template) { match ->
        val value = paramValues[match.getParamName()]!!
        if (match.isMapping()) {
            if (value.definition.type == ParamDefinition.Type.Flag) {
                if (value.value.toBoolean()) {
                    match.getMappedFlag()
                } else {
                    emptyString()
                }
            } else {
                if (value.value.isNotEmpty()) {
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

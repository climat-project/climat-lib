package template

import domain.ActionValueBase
import domain.CustomScriptActionValue
import domain.ScopeParamsActionValue
import domain.TemplateActionValue
import domain.ref.Ref
import domain.ref.RefWithValue
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
                match.getMappedFlag()
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

internal fun setActualCommand(
    action: ActionValueBase<*>,
    values: Map<String, RefWithValue>
) {
    when (action) {
        is TemplateActionValue -> {
            val missingValues = getParamReferences(action.template)
                .map { it.paramName }
                .distinct()
                .filter(not(values::contains))
                .toList()
            if (missingValues.any()) {
                throw Exception(
                    "Cannot compile command. Missing parameters: ${
                    missingValues.joinToString(", ")
                    }"
                )
            }
            action.value = actionRe.replace(action.template) { match ->
                val value = values[match.getParamName()]!!
                if (match.isMapping()) {
                    if (value.ref.type == Ref.Type.Flag) {
                        if (value.value.toBoolean()) {
                            match.getMappedFlag()
                        } else {
                            emptyString()
                        }
                    } else {
                        if (value.value.isNotEmpty()) {
                            "${match.getMappedFlag()}=${value.value}"
                        } else {
                            emptyString()
                        }
                    }
                } else {
                    value.value
                }
            }
                // Remove duplicate whitespaces
                .replace("\\s+".toRegex(), " ")
        }
        is CustomScriptActionValue -> {
            // handled by the library consumer
        }
        is ScopeParamsActionValue -> {
            action.value = values.mapValues { it.value.value }
        }
        else -> throw Exception("Type `${action::class}` not supported")
    }
}

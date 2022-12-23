package validation

import Toolchain
import validation.validations.*

private val actionRe = Regex("\\\$\\(([\\w.]+)(?::([^ ()]+))?\\)")
private val validators = listOf(
    DuplicateChildrenNames(),
    DuplicateParamNames(),
    FlagMappings(),
    ShadowedParams(),
    UndefinedParams()
)

private fun computeValidations(current: Toolchain,
              paramsDefinedAbove: Set<Toolchain.Parameter> = emptySet()): Sequence<ValidationResult> =
    ValidationContext(
        scopeParams = (paramsDefinedAbove + current.parameters.orEmpty()).groupBy { it.name },
        regexMatches = actionRe.findAll(current.action),
        toolchain = current
    ).let { context ->
        validators.flatMap { validator -> validator.validate(context).map {
            ValidationResult(it, validator.type, current)
        } } +
        current.children.orEmpty().flatMap {
            computeValidations(it, context.scopeParams.values.flatten().toSet())
        }
    }.asSequence()

fun validate(toolchain: Toolchain) {
    val validations = computeValidations(toolchain)
    val warnings = validations.filter { it.type == ValidationResult.ValidationEntryType.Warning }
    if (warnings.any()) {
        println(warnings.joinToString(", "))
    }

    val errors = validations.filter { it.type == ValidationResult.ValidationEntryType.Error }
    if (errors.any()) {
        throw Exception(errors.joinToString(", "))
    }
}
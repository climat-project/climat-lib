package validation

import Toolchain
import validation.validations.DuplicateChildrenNames
import validation.validations.DuplicateParamNames
import validation.validations.FlagMappings
import validation.validations.UndefinedParams

private val validators = listOf(
    DuplicateChildrenNames(),
    DuplicateParamNames(),
    FlagMappings(),
    UndefinedParams()
)

// Made public only for testing
fun computeValidations(
    current: Toolchain,
    paramsDefinedAbove: Set<Toolchain.Parameter> = emptySet()
): Sequence<ValidationResult> =
    ValidationContext(
        scopeParams = (paramsDefinedAbove + current.parameters.orEmpty()).groupBy { it.name },
        toolchain = current
    ).let { context ->
        validators.flatMap { validator ->
            validator.validate(context).map {
                ValidationResult(it, validator.type, current)
            }
        } +
            current.children.orEmpty().flatMap {
                computeValidations(it, context.scopeParams.values.flatten().toSet())
            }
    }.asSequence()

// TODO implement warning for unused variables
// TODO implement warning for having the same name as an ancestor
// TODO implement warning for a flag mapped twice
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

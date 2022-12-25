package validation

import Toolchain
import newLine
import validation.validations.AncestorSubommandWithSameName
import validation.validations.DuplicateChildrenNames
import validation.validations.DuplicateParamNames
import validation.validations.FlagMappedTwice
import validation.validations.FlagMappings
import validation.validations.ShadowedParams
import validation.validations.UndefinedParams

private val validators = listOf(
    DuplicateChildrenNames(),
    DuplicateParamNames(),
    FlagMappings(),
    UndefinedParams(),
    FlagMappedTwice(),
    AncestorSubommandWithSameName(),
    ShadowedParams()
)

// Made public only for testing
internal fun computeValidations(
    current: Toolchain,
    pathToRoot: List<Toolchain> = emptyList()
): Sequence<ValidationResult> =
    (
        validators.flatMap { validator ->
            validator.validate(
                ValidationContext(
                    pathToRoot = pathToRoot,
                    toolchain = current
                )
            ).map {
                ValidationResult(it, validator.type, current)
            }
        } +
            current.children.orEmpty().flatMap {
                computeValidations(it, pathToRoot + listOf(current))
            }
        ).asSequence()

// TODO implement warning for unused variables
// TODO implement warning for a flag mapped twice
internal fun validate(toolchain: Toolchain) {
    val validations = computeValidations(toolchain)
    val warnings = validations.filter { it.type == ValidationResult.ValidationEntryType.Warning }
    if (warnings.any()) {
        println(warnings.joinToString(newLine()))
    }

    val errors = validations.filter { it.type == ValidationResult.ValidationEntryType.Error }
    if (errors.any()) {
        throw Exception(errors.joinToString(newLine()))
    }
}

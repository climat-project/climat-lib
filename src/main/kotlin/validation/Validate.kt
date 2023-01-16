package validation

import domain.toolchain.RootToolchain
import domain.toolchain.Toolchain
import newLine
import validation.validations.AncestorSubcommandWithSameName
import validation.validations.BooleanFlippedMappings
import validation.validations.DefaultForFlag
import validation.validations.DefaultForRequiredParam
import validation.validations.DefaultForUndefinedParam
import validation.validations.DuplicateRefNames
import validation.validations.DuplicateToolchainNamesOrAliases
import validation.validations.FlagMappedTwice
import validation.validations.ShadowedParams
import validation.validations.UndefinedParams
import validation.validations.UselessToolchain

private val validators = listOf(
    AncestorSubcommandWithSameName(),
    BooleanFlippedMappings(),
    DefaultForFlag(),
    DefaultForRequiredParam(),
    DefaultForUndefinedParam(),
    DuplicateToolchainNamesOrAliases(),
    DuplicateRefNames(),
    FlagMappedTwice(),
    ShadowedParams(),
    UndefinedParams(),
    UselessToolchain()
)

// Made internal only for testing
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
                ValidationResult(it, validator.code, validator.type, current)
            }
        } +
            current.children.flatMap {
                computeValidations(it, pathToRoot + listOf(current))
            }
        ).asSequence()

// TODO implement warning for unused variables
internal fun validate(toolchain: RootToolchain) {
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

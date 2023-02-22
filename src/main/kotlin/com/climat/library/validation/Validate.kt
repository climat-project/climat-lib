package com.climat.library.validation

import com.climat.library.domain.toolchain.RootToolchain
import com.climat.library.domain.toolchain.Toolchain
import com.climat.library.utils.newLine
import com.climat.library.validation.validations.AncestorSubcommandWithSameName
import com.climat.library.validation.validations.BooleanFlippedMappings
import com.climat.library.validation.validations.DefaultForFlag
import com.climat.library.validation.validations.DefaultForRequiredParam
import com.climat.library.validation.validations.DuplicateRefNames
import com.climat.library.validation.validations.DuplicateToolchainNamesOrAliases
import com.climat.library.validation.validations.FlagMappedTwice
import com.climat.library.validation.validations.ShadowedParams
import com.climat.library.validation.validations.UndefinedParams
import com.climat.library.validation.validations.UselessToolchain

private val validators = listOf(
    AncestorSubcommandWithSameName(),
    BooleanFlippedMappings(),
    DefaultForFlag(),
    DefaultForRequiredParam(),
    DuplicateToolchainNamesOrAliases(),
    DuplicateRefNames(),
    FlagMappedTwice(),
    ShadowedParams(),
    UndefinedParams(),
    UselessToolchain()
)

internal fun computeValidations(
    current: RootToolchain
) = computeValidations(current, current.sourceCode)

private fun computeValidations(
    current: Toolchain,
    sourceCode: String,
    pathToRoot: List<Toolchain> = emptyList(),
): Sequence<ValidationResult> =
    (
        validators.flatMap { validator ->
            validator.validate(
                ValidationContext(
                    pathToRoot = pathToRoot,
                    toolchain = current
                )
            ).map {
                ValidationResult(it.message, sourceCode, it.sourceMap, validator.code, validator.type, current)
            }
        } +
            current.children.flatMap {
                computeValidations(it, sourceCode, pathToRoot + listOf(current))
            }
        ).asSequence()

// TODO implement warning for unused variables
internal fun validate(toolchain: RootToolchain) {
    val validations = computeValidations(toolchain)
    val errors = validations.filter { it.type == ValidationResult.ValidationEntryType.Error }.toList()
    if (errors.any()) {
        throw Exception(errors.joinToString(newLine()))
    }
}

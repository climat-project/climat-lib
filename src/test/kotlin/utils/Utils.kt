package utils

import ToolchainDto
import domain.convert
import validation.ValidationResult
import validation.computeValidations

private fun ToolchainDto.getValidations(validationType: ValidationResult.ValidationEntryType) =
    computeValidations(
        convert(this)
    ).filter { it.type == validationType }

internal fun ToolchainDto.getErrors() = this.getValidations(ValidationResult.ValidationEntryType.Error)
internal fun ToolchainDto.getWarnings() = this.getValidations(ValidationResult.ValidationEntryType.Warning)

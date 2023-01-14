package utils

import ToolchainDto
import domain.convert
import validation.computeValidations
import validation.validations.ValidationCode

internal fun ToolchainDto.getValidations(code: ValidationCode) =
    computeValidations(
        convert(this)
    ).filter { it.code == code }

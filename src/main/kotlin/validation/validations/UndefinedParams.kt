package validation.validations

import not
import template.getParamReferences
import validation.IValidation
import validation.ValidationContext
import validation.ValidationResult

class UndefinedParams : IValidation {
    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = "0001"

    override fun validate(ctx: ValidationContext): Sequence<String> =
        getParamReferences(ctx.toolchain.action)
            .map { it.paramName }
            .distinct()
            .filter(not(ctx.scopeParams::contains))
            .map {
                "Parameter `$it` is not defined in the current scope"
            }
}

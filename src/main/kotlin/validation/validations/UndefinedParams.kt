package validation.validations

import not
import validation.IValidation
import validation.ValidationContext
import validation.ValidationResult

class UndefinedParams : IValidation {
    override val type
        get() = ValidationResult.ValidationEntryType.Error
    override val code: String
        get() = "0001"

    override fun validate(ctx: ValidationContext): Sequence<String> =
        ctx.regexMatches
           .map { it.groupValues[1] }
           .distinct()
           .filter(not(ctx.scopeParams::contains))
           .map {
               "Parameter `$it` is not defined in the current scope"
           }
}
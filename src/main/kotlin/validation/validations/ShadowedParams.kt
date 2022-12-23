package validation.validations

import validation.IValidation
import validation.ValidationContext
import validation.ValidationResult

class ShadowedParams : IValidation {
    override val type
        get() = ValidationResult.ValidationEntryType.Error
    override val code: String
        get() = "0005"

    // Algorithm is wrong, but does the work for now
    override fun validate(ctx: ValidationContext): Sequence<String> =
        ctx.scopeParams
            .values.asSequence()
            .filter { it.size == 2 }
            .map {
                "Parameter `${it.first().name}` is shadowed"
            }
}

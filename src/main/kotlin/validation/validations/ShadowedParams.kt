package validation.validations

import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

class ShadowedParams : ValidationBase() {
    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = "0005"

    // Algorithm is wrong, but does the work for now
    override fun validate(ctx: ValidationContext): Sequence<String> =
        getScopeParams(ctx)
            .values.asSequence()
            .filter { it.size == 2 }
            .map {
                "Parameter `${it.first().name}` is shadowed"
            }
}

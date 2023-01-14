package validation.validations

import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

internal class ShadowedParams : ValidationBase() {
    // Check if it needs to be warning or error
    override val type get() = ValidationResult.ValidationEntryType.Warning
    override val code get() = ValidationCode.ShadowedParams

    // Algorithm is wrong, but does the work for now
    override fun validate(ctx: ValidationContext): Sequence<String> =
        getScopeRefs(ctx)
            .values.asSequence()
            .filter { it.size == 2 }
            .map {
                "Parameter `${it.first().name}` is shadowed"
            }
}

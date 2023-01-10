package validation.validations

import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

internal class DefaultForRequiredParam : ValidationBase() {

    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = "0010"

    override fun validate(ctx: ValidationContext): Sequence<String> =
        (
            getScopeParams(ctx).filter { (_, v) -> !v.last().optional }
                .keys
                .intersect(getDefaultParamKeys(ctx))
            ).let { defaultForRequired ->
            defaultForRequired.map {
                "Param `$it` is required, and should not have default"
            }
        }.asSequence()
}

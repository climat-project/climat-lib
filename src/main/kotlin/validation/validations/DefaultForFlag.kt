package validation.validations

import domain.ParamDefinition
import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

internal class DefaultForFlag : ValidationBase() {

    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = ValidationCode.DefaultForFlag

    override fun validate(ctx: ValidationContext): Sequence<String> =
        (
            getScopeParams(ctx).filter { (_, v) -> v.last().type == ParamDefinition.Type.Flag }
                .keys
                .intersect(getDefaultParamKeys(ctx))
            ).let { defaultForRequired ->
            defaultForRequired.map {
                "Cannot set default to flag `$it`. For a flag the default" +
                    "value is always false. You flip param value when using the parameter" +
                    "eg: $(!flipped|--flag)"
            }
        }.asSequence()
}

package validation.validations

import domain.Toolchain
import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

internal class DefaultForFlag : ValidationBase() {

    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = "0009"

    override fun validate(ctx: ValidationContext): Sequence<String> =
        (
            getScopeParams(ctx).filter { (_, v) -> v.last().type == Toolchain.Type.Flag }
                .keys
                .intersect(ctx.toolchain.paramDefaults?.keys.orEmpty())
            ).let { defaultForRequired ->
            defaultForRequired.map {
                "Cannot set default to flag `$it`. For a flag the default" +
                    "value is always false. You flip param value when using the parameter" +
                    "eg: $(!flipped|--flag)"
            }
        }.asSequence()
}

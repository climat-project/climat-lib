package validation.validations

import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

internal class DefaultForUndefinedParam : ValidationBase() {
    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = "0008"

    override fun validate(ctx: ValidationContext): Sequence<String> =
        (
            ctx.toolchain.paramDefaults?.keys.orEmpty() -
                getScopeParams(ctx).keys
            ).let { undefinedDefaults ->
            undefinedDefaults.map {
                "No param definition for `$it` default"
            }
        }.asSequence()
}

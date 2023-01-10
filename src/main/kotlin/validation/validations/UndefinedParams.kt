package validation.validations

import not
import template.getParamReferences
import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

internal class UndefinedParams : ValidationBase() {
    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = "0001"

    override fun validate(ctx: ValidationContext): Sequence<String> =
        getScopeParams(ctx).let { scopeParams ->
            getParamReferences(ctx.toolchain.action.template)
                .map { it.paramName }
                .distinct()
                .filter(not(scopeParams::contains))
                .map {
                    "Parameter `$it` is not defined in the current scope"
                }
        }
}

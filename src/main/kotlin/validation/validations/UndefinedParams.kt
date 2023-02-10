package validation.validations

import domain.action.TemplateActionValue
import not
import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

internal class UndefinedParams : ValidationBase() {
    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = ValidationCode.UndefinedParams

    override fun validate(ctx: ValidationContext): Sequence<String> =
        getScopeRefs(ctx).let { scopeParams ->
            val act = ctx.toolchain.action
            if (act is TemplateActionValue)
                act.template.refReferences
                    .asSequence()
                    .map { it.name }
                    .distinct()
                    .filter(not(scopeParams::contains))
                    .map {
                        "Parameter `$it` is not defined in the current scope"
                    }
            else emptySequence()
        }
}

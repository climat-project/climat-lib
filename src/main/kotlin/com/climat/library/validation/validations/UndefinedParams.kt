package com.climat.library.validation.validations

import com.climat.library.domain.action.TemplateActionValue
import com.climat.library.utils.not
import com.climat.library.validation.ValidationBase
import com.climat.library.validation.ValidationContext
import com.climat.library.validation.ValidationResult

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

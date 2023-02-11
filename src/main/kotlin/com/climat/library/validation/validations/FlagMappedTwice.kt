package com.climat.library.validation.validations

import com.climat.library.domain.action.TemplateActionValue
import com.climat.library.validation.ValidationBase
import com.climat.library.validation.ValidationContext
import com.climat.library.validation.ValidationResult

internal class FlagMappedTwice : ValidationBase() {
    // TODO think if it should be warning or error
    override val type get() = ValidationResult.ValidationEntryType.Warning
    override val code get() = ValidationCode.FlagMappedTwice
    override fun validate(ctx: ValidationContext): Sequence<String> =
        ctx.toolchain.action.let { act ->
            if (act is TemplateActionValue) {
                act.template.refReferences
                    .filter { it.mapping != null }
                    .groupBy { it.mapping }
                    .values.asSequence()
                    .filter { it.size >= 2 }
                    .map { it.first().mapping!! }
                    .map { "Flag $it was mapped more than once" }
            } else { emptySequence() }
        }
}

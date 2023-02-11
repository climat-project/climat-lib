package com.climat.library.validation.validations

import com.climat.library.domain.action.TemplateActionValue
import com.climat.library.domain.ref.Ref
import com.climat.library.validation.ValidationBase
import com.climat.library.validation.ValidationContext
import com.climat.library.validation.ValidationResult

internal class BooleanFlippedMappings : ValidationBase() {
    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = ValidationCode.BooleanFlippedMappings

    override fun validate(ctx: ValidationContext): Sequence<String> =
        ctx.toolchain.action.let { act ->
            if (act is TemplateActionValue) {
                getScopeRefs(ctx)
                    .values
                    .map { it.last() }
                    .filter { it.type != Ref.Type.Flag }
                    .map { it.name }
                    .intersect(
                        act.template.refReferences
                            .filter { it.isFlipped }
                            .map { it.name }
                            .toSet()
                    ).map {
                        "Param `$it` cannot be flipped because it is not a flag"
                    }.asSequence()
            } else {
                emptySequence()
            }
        }
}

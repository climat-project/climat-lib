package com.climat.library.validation.validations

import com.climat.library.validation.ValidationBase
import com.climat.library.validation.ValidationContext
import com.climat.library.validation.ValidationEntry
import com.climat.library.validation.ValidationResult

internal class ShadowedParams : ValidationBase() {
    // Check if it needs to be warning or error
    override val type get() = ValidationResult.ValidationEntryType.Warning
    override val code get() = ValidationCode.ShadowedParams

    // Algorithm is wrong, but does the work for now
    override fun validate(ctx: ValidationContext): Sequence<ValidationEntry> =
        getScopeRefs(ctx)
            .values.asSequence()
            .filter { it.size == 2 }
            .map {
                val first = it.first()
                ValidationEntry("Parameter `${first.name}` is shadowed", first.sourceMap)
            }
}

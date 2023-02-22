package com.climat.library.validation.validations

import com.climat.library.domain.ref.Ref
import com.climat.library.validation.ValidationBase
import com.climat.library.validation.ValidationContext
import com.climat.library.validation.ValidationEntry
import com.climat.library.validation.ValidationResult

internal class DefaultForFlag : ValidationBase() {

    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = ValidationCode.DefaultForFlag

    override fun validate(ctx: ValidationContext): Sequence<ValidationEntry> =
        (
            getScopeParams(ctx)
                .values
                .map { it.last() }
                .filter { it.type == Ref.Type.Flag && it.default != null }
            ).let { defaultForRequired ->
            defaultForRequired.map {
                ValidationEntry(
                    "Cannot set default to flag `${it.name}`. For a flag the default" +
                        "value is always false. You flip param value when using the parameter",
                    it.sourceMap
                )
            }
        }.asSequence()
}

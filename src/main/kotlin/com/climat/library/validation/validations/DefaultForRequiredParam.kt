package com.climat.library.validation.validations

import com.climat.library.validation.ValidationBase
import com.climat.library.validation.ValidationContext
import com.climat.library.validation.ValidationResult

internal class DefaultForRequiredParam : ValidationBase() {

    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = ValidationCode.DefaultForRequiredParam

    override fun validate(ctx: ValidationContext): Sequence<String> =
        (
            getScopeParams(ctx).filter { (_, v) -> !v.last().optional && v.last().default != null }
                .keys
            ).let { defaultForRequired ->
            defaultForRequired.map {
                "Param `$it` is required, and should not have default"
            }
        }.asSequence()
}

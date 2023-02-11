package com.climat.library.validation.validations

import com.climat.library.validation.ValidationBase
import com.climat.library.validation.ValidationContext
import com.climat.library.validation.ValidationResult

internal class DefaultForUndefinedParam : ValidationBase() {
    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = ValidationCode.DefaultForUndefinedParam

    override fun validate(ctx: ValidationContext): Sequence<String> =
        (
            getDefaultParamKeys(ctx) -
                getScopeParams(ctx).keys
            ).let { undefinedDefaults ->
            undefinedDefaults.map {
                "No param definition for `$it` default"
            }
        }.asSequence()
}

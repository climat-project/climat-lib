package com.climat.library.validation.validations

import com.climat.library.validation.ValidationBase
import com.climat.library.validation.ValidationContext
import com.climat.library.validation.ValidationEntry
import com.climat.library.validation.ValidationResult

internal class AncestorSubcommandWithSameName : ValidationBase() {
    override val type get() = ValidationResult.ValidationEntryType.Warning
    override val code get() = ValidationCode.AncestorSubcommandWithSameName

    override fun validate(ctx: ValidationContext): Sequence<ValidationEntry> =
        ctx.pathToRoot
            .find { it.name == ctx.toolchain.name }
            ?.let {
                sequenceOf(
                    it.validationEntry("There is already an ancestor with name ${it.name}")
                )
            }
            ?: emptySequence()
}

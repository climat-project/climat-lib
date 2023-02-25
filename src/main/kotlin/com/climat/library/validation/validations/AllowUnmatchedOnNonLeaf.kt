package com.climat.library.validation.validations

import com.climat.library.domain.isLeaf
import com.climat.library.validation.ValidationBase
import com.climat.library.validation.ValidationContext
import com.climat.library.validation.ValidationEntry
import com.climat.library.validation.ValidationResult

internal class AllowUnmatchedOnNonLeaf : ValidationBase() {
    override val type: ValidationResult.ValidationEntryType
        get() = ValidationResult.ValidationEntryType.Error
    override val code: ValidationCode
        get() = ValidationCode.AllowUnmatchedOnNonLeaf

    override fun validate(ctx: ValidationContext): Sequence<ValidationEntry> =
        if (ctx.toolchain.allowUnmatched && !ctx.toolchain.isLeaf) {
            sequenceOf(
                ValidationEntry(
                    message = "allow-unmatched modifier is not allowed on ${ctx.toolchain.name}, but only on leaf toolchains",
                    sourceMap = ctx.toolchain.sourceMap // TODO: display only the modifier
                )
            )
        } else {
            emptySequence()
        }
}

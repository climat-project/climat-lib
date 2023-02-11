package com.climat.library.validation.validations

import com.climat.library.domain.refs
import com.climat.library.validation.ValidationBase
import com.climat.library.validation.ValidationContext
import com.climat.library.validation.ValidationResult

internal class DuplicateRefNames : ValidationBase() {
    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = ValidationCode.DuplicateRefNames

    override fun validate(ctx: ValidationContext): Sequence<String> =
        ctx.toolchain.refs
            .groupBy { it.name }
            .asSequence()
            .filter { (_, v) -> v.size >= 2 }
            .map { (k, _) -> "Duplicate parameter `$k`" }
}

package validation.validations

import domain.refs
import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

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

package validation.validations

import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

internal class DuplicateChildrenNames : ValidationBase() {
    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = ValidationCode.DuplicateChildrenNames

    override fun validate(ctx: ValidationContext): Sequence<String> =
        ctx.toolchain.children
            .groupBy { it.name }
            .asSequence()
            .filter { (_, v) -> v.size >= 2 }
            .map { (k, _) -> "Duplicate child name `$k`" }
}

package validation.validations

import validation.IValidation
import validation.ValidationContext
import validation.ValidationResult

class DuplicateChildrenNames : IValidation {
    override val type
        get() = ValidationResult.ValidationEntryType.Error
    override val code: String
        get() = "0002"
    override fun validate(ctx: ValidationContext): Sequence<String> =
        ctx.toolchain.children
            .orEmpty()
            .groupBy { it.name }
            .asSequence()
            .filter { (_, v) -> v.size >= 2 }
            .map { (k, _) -> "Duplicate child name `$k`" }
}
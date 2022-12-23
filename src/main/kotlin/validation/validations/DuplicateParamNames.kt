package validation.validations

import validation.IValidation
import validation.ValidationContext
import validation.ValidationResult

class DuplicateParamNames : IValidation {
    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = "0003"

    override fun validate(ctx: ValidationContext): Sequence<String> =
        ctx.toolchain.parameters
            .orEmpty()
            .groupBy { it.name }
            .asSequence()
            .filter { (_, v) -> v.size >= 2 }
            .map { (k, _) -> "Duplicate parameter `$k`" }
}

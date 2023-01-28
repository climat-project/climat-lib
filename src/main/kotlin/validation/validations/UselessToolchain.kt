package validation.validations

import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

internal class UselessToolchain : ValidationBase() {
    override val type get() = ValidationResult.ValidationEntryType.Warning
    override val code get() = ValidationCode.UselessToolchain

    override fun validate(ctx: ValidationContext): Sequence<String> =
        ctx.toolchain.let {
            if (it.action.template.isEmpty() && it.children.isEmpty()) {
                sequenceOf("`${it.name}` toolchain is useless. Has no children or action")
            } else {
                emptySequence()
            }
        }
}
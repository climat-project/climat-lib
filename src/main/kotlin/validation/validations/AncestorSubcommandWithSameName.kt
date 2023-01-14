package validation.validations

import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

internal class AncestorSubcommandWithSameName : ValidationBase() {
    override val type get() = ValidationResult.ValidationEntryType.Warning
    override val code get() = ValidationCode.AncestorSubcommandWithSameName

    override fun validate(ctx: ValidationContext): Sequence<String> =
        ctx.pathToRoot
            .map { it.name }
            .find { it == ctx.toolchain.name }
            .let {
                if (it != null) {
                    listOf("There is already an ancestor with name $it")
                } else {
                    emptyList()
                }
            }.asSequence()
}
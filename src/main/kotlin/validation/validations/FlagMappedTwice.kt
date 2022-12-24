package validation.validations

import template.getParamReferences
import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

class FlagMappedTwice : ValidationBase() {
    // TODO think if it should be warning or error
    override val type get() = ValidationResult.ValidationEntryType.Warning
    override val code get() = "0008"
    override fun validate(ctx: ValidationContext): Sequence<String> =
        getParamReferences(ctx.toolchain.action)
            .filter { it.flagMapTarget != null }
            .groupBy { it.flagMapTarget }
            .values.asSequence()
            .filter { it.size >= 2 }
            .map { it.first() }
            .map { "Flag ${it.flagMapTarget} was mapped more than once" }
}

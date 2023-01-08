package validation.validations

import template.getParamReferences
import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

internal class FlagMappedTwice : ValidationBase() {
    // TODO think if it should be warning or error
    override val type get() = ValidationResult.ValidationEntryType.Warning
    override val code get() = "0008"
    override fun validate(ctx: ValidationContext): Sequence<String> =
        getParamReferences(ctx.toolchain.parsedAction)
            .filter { it.mapTarget != null }
            .groupBy { it.mapTarget }
            .values.asSequence()
            .filter { it.size >= 2 }
            .map { it.first() }
            .map { "Flag ${it.mapTarget} was mapped more than once" }
}

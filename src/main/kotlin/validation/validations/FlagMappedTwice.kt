package validation.validations

import domain.action.TemplateActionValue
import template.getParamReferences
import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

internal class FlagMappedTwice : ValidationBase() {
    // TODO think if it should be warning or error
    override val type get() = ValidationResult.ValidationEntryType.Warning
    override val code get() = ValidationCode.FlagMappedTwice
    override fun validate(ctx: ValidationContext): Sequence<String> =
        ctx.toolchain.action.let { act ->
            if (act is TemplateActionValue) {
                getParamReferences(act.template)
                    .filter { it.mapTarget != null }
                    .groupBy { it.mapTarget }
                    .values.asSequence()
                    .filter { it.size >= 2 }
                    .map { it.first().mapTarget!! }
                    .map { "Flag $it was mapped more than once" }
            } else { emptySequence() }
        }
}

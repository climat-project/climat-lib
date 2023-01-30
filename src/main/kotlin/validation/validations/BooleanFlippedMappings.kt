package validation.validations

import domain.TemplateActionValue
import domain.ref.Ref
import template.getParamReferences
import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

internal class BooleanFlippedMappings : ValidationBase() {
    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = ValidationCode.BooleanFlippedMappings

    override fun validate(ctx: ValidationContext): Sequence<String> =
        ctx.toolchain.action.let { act ->
            if (act is TemplateActionValue) {
                getScopeRefs(ctx)
                    .values
                    .map { it.last() }
                    .filter { it.type != Ref.Type.Flag }
                    .map { it.name }
                    .intersect(
                        getParamReferences(act.template)
                            .filter { it.isFlipped }
                            .map { it.paramName }
                            .toSet()
                    ).map {
                        "Param `$it` cannot be flipped because it is not a flag"
                    }.asSequence()
            } else {
                emptySequence()
            }
        }
}

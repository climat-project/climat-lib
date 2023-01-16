package validation.validations

import domain.ref.Ref
import template.getParamReferences
import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

internal class BooleanFlippedMappings : ValidationBase() {
    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = ValidationCode.BooleanFlippedMappings

    override fun validate(ctx: ValidationContext): Sequence<String> =
        getScopeRefs(ctx)
            .values
            .map { it.last() }
            .filter { it.type != Ref.Type.Flag }
            .map { it.name }
            .intersect(
                getParamReferences(ctx.toolchain.action.template)
                    .filter { it.isFlipped }
                    .map { it.paramName }
                    .toSet()
            ).map {
                "Param `$it` cannot be flipped because it is not a flag"
            }.asSequence()
}

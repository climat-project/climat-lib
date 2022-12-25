package validation.validations

import template.getParamReferences
import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

internal class FlagMappings : ValidationBase() {
    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = "0004"

    override fun validate(ctx: ValidationContext): Sequence<String> =
        getScopeParams(ctx).let { scopeParams ->
            getParamReferences(ctx.toolchain.action)
                .filter { it.flagMapTarget != null }
                .map { it.paramName }
                .mapNotNull { scopeParams[it]?.last() }
                .filter { it.type != Toolchain.Type.bool }
                .map {
                    "Parameter `${it.name}` is used " +
                        "as a flag mapping, therefore should have type ${Toolchain.Type.bool.name}"
                }
        }
}

package validation.validations

import template.getParamReferences
import validation.IValidation
import validation.ValidationContext
import validation.ValidationResult

class FlagMappings : IValidation {
    override val type
        get() = ValidationResult.ValidationEntryType.Error
    override val code: String
        get() = "0004"

    override fun validate(ctx: ValidationContext): Sequence<String> =
        getParamReferences(ctx.toolchain.action)
            .filter { it.flagMapTarget != null }
            .map { it.paramName }
            .mapNotNull { ctx.scopeParams[it]?.last() }
            .filter { it.type != Toolchain.Type.bool }
            .map {
                "Parameter `${it.name}` is used " +
                    "as a flag mapping, therefore should have type ${Toolchain.Type.bool.name}"
            }
}

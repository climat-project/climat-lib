package validation.validations

import validation.IValidation
import validation.ValidationContext
import validation.ValidationResult

class FlagMappings : IValidation {
    override val type
        get() = ValidationResult.ValidationEntryType.Error
    override val code: String
        get() = "0004"

    override fun validate(ctx: ValidationContext): Sequence<String> =
        ctx.regexMatches.filter { println(it.groups); it.groups[2] != null }
            .map { it.groupValues[1] }
            .mapNotNull { ctx.scopeParams[it]?.last() }
            .filter { it.type != Toolchain.Type.bool }
            .map {
                "Parameter `${it.name}` is used " +
                    "as a flag mapping, therefore should have type ${Toolchain.Type.bool.name}"
            }
}

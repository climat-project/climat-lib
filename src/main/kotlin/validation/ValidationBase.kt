package validation

import domain.ParamDefinition
import validation.validations.ValidationCode

internal abstract class ValidationBase {
    internal abstract val type: ValidationResult.ValidationEntryType
    internal abstract val code: ValidationCode
    internal abstract fun validate(ctx: ValidationContext): Sequence<String>
    protected fun getScopeParams(ctx: ValidationContext): Map<String, List<ParamDefinition>> =
        (
            ctx.pathToRoot.flatMap { it.parameters.asSequence() } +
                ctx.toolchain.parameters
            )
            .groupBy { it.name }

    protected fun getDefaultParamKeys(ctx: ValidationContext): Set<String> =
        ctx.toolchain.parameterDefaults.keys
}

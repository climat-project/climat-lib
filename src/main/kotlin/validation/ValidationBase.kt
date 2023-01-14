package validation

import validation.validations.ValidationCode

internal abstract class ValidationBase {
    internal abstract val type: ValidationResult.ValidationEntryType
    internal abstract val code: ValidationCode
    internal abstract fun validate(ctx: ValidationContext): Sequence<String>
    protected fun getScopeParams(ctx: ValidationContext) =
        (
            ctx.pathToRoot.flatMap { it.parameters.asSequence() } +
                ctx.toolchain.parameters
            )
            .groupBy { it.name }

    protected fun getDefaultParamKeys(ctx: ValidationContext) =
        ctx.toolchain.parameters.filter { it.default != null }.map { it.name }.toSet()
}

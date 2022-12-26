package validation

internal abstract class ValidationBase {
    internal abstract val type: ValidationResult.ValidationEntryType
    internal abstract val code: String
    internal abstract fun validate(ctx: ValidationContext): Sequence<String>
    protected fun getScopeParams(ctx: ValidationContext) =
        (
            ctx.pathToRoot.flatMap { it.parsedParameters.asSequence() } +
                ctx.toolchain.parsedParameters
            )
            .groupBy { it.name }
}

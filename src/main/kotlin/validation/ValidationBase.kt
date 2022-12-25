package validation

internal abstract class ValidationBase {
    abstract val type: ValidationResult.ValidationEntryType
    abstract val code: String
    abstract fun validate(ctx: ValidationContext): Sequence<String>
    protected fun getScopeParams(ctx: ValidationContext) =
        (
            ctx.pathToRoot.flatMap { it.parsedParameters.asSequence() } +
                ctx.toolchain.parsedParameters
            )
            .groupBy { it.name }
}

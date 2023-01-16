package validation

import domain.ref.ParamDefinition
import domain.ref.Ref
import domain.refs
import validation.validations.ValidationCode

internal abstract class ValidationBase {
    internal abstract val type: ValidationResult.ValidationEntryType
    internal abstract val code: ValidationCode
    internal abstract fun validate(ctx: ValidationContext): Sequence<String>

    protected fun getScopeRefs(ctx: ValidationContext): Map<String, List<Ref>> =
        (ctx.pathToRoot.flatMap { it.refs } + ctx.toolchain.refs)
            .groupBy { it.name }

    protected fun getScopeParams(ctx: ValidationContext): Map<String, List<ParamDefinition>> =
        getScopeRefs(ctx)
            .mapValues { it.value.filterIsInstance<ParamDefinition>() }
            .filterValues { it.isNotEmpty() }

    protected fun getDefaultParamKeys(ctx: ValidationContext): Set<String> =
        ctx.toolchain.parameterDefaults.keys
}

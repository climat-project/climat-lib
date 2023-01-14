package validation

import domain.ParamDefinition
import domain.Ref
import domain.Toolchain
import validation.validations.ValidationCode

internal abstract class ValidationBase {
    internal abstract val type: ValidationResult.ValidationEntryType
    internal abstract val code: ValidationCode
    internal abstract fun validate(ctx: ValidationContext): Sequence<String>

    // TODO: extract as a extension method / Toolchain class method
    protected fun Refs(toolchain: Toolchain): List<Ref> =
        toolchain.constants.toList() +
            toolchain.parameters.toList()

    protected fun getScopeRefs(ctx: ValidationContext): Map<String, List<Ref>> =
        (ctx.pathToRoot.flatMap { Refs(it) } + Refs(ctx.toolchain))
            .groupBy { it.name }

    protected fun getScopeParams(ctx: ValidationContext): Map<String, List<ParamDefinition>> =
        getScopeRefs(ctx).mapValues { it.value.filterIsInstance<ParamDefinition>() }

    protected fun getDefaultParamKeys(ctx: ValidationContext): Set<String> =
        ctx.toolchain.parameterDefaults.keys
}

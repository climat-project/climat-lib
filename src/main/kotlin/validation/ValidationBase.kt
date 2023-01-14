package validation

import domain.ParamDefinition
import domain.Referenceable
import domain.Toolchain
import validation.validations.ValidationCode

internal abstract class ValidationBase {
    internal abstract val type: ValidationResult.ValidationEntryType
    internal abstract val code: ValidationCode
    internal abstract fun validate(ctx: ValidationContext): Sequence<String>

    // TODO: extract as a extension method / Toolchain class method
    protected fun referenceables(toolchain: Toolchain): List<Referenceable> =
        toolchain.constants.toList() +
            toolchain.parameters.toList()

    protected fun getScopeReferenceables(ctx: ValidationContext): Map<String, List<Referenceable>> =
        (ctx.pathToRoot.flatMap { referenceables(it) } + referenceables(ctx.toolchain))
            .groupBy { it.name }

    protected fun getScopeParams(ctx: ValidationContext): Map<String, List<ParamDefinition>> =
        getScopeReferenceables(ctx).mapValues { it.value.filterIsInstance<ParamDefinition>() }

    protected fun getDefaultParamKeys(ctx: ValidationContext): Set<String> =
        ctx.toolchain.parameterDefaults.keys
}

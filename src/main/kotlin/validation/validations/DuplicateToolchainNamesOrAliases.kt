package validation.validations

import domain.eachAlias
import validation.ValidationBase
import validation.ValidationContext
import validation.ValidationResult

internal class DuplicateToolchainNamesOrAliases : ValidationBase() {
    override val type get() = ValidationResult.ValidationEntryType.Error
    override val code get() = ValidationCode.DuplicateToolchainNamesOrAliases

    override fun validate(ctx: ValidationContext): Sequence<String> =
        ctx.toolchain.children
            .flatMap { it.eachAlias }
            .groupBy { it.name }
            .asSequence()
            .filter { (_, v) -> v.size >= 2 }
            .map { (k, _) -> "Duplicate child name or alias `$k`" }
}

package validation

import domain.Toolchain
import validation.validations.ValidationCode

internal data class ValidationResult(
    private val message: String,
    val code: ValidationCode,
    val type: ValidationEntryType,
    private val toolchain: Toolchain
) {
    private var repr: String

    init {
        repr = "${
        when (type) {
            ValidationEntryType.Error -> "Error"
            ValidationEntryType.Warning -> "Warning"
        }
        }: in `${toolchain.name}`, $message"
    }

    enum class ValidationEntryType {
        Warning,
        Error
    }

    override fun toString() = repr
}

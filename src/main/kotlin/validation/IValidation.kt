package validation

interface IValidation {
    val type: ValidationResult.ValidationEntryType

    val code: String
    fun validate(ctx: ValidationContext): Sequence<String>
}

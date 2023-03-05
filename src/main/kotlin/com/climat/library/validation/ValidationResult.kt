package com.climat.library.validation

import com.climat.library.dslParser.exception.getSourceCodeErrorCaretIndicator
import com.climat.library.utils.emptyString
import com.climat.library.utils.newLine
import com.climat.library.validation.validations.ValidationCode
import org.antlr.v4.kotlinruntime.ast.Position
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class ValidationResult internal constructor(
    message: String,
    private val sourceCode: String,
    sourceMap: Position?,
    val code: ValidationCode,
    val type: ValidationEntryType
) {
    private var repr: String

    init {
        repr = "${
        when (type) {
            ValidationEntryType.Error -> "Error"
            ValidationEntryType.Warning -> "Warning"
        }
        }: $message ${
        sourceMap?.let {
            newLine() + getSourceCodeErrorCaretIndicator(
                sourceCode,
                it
            )
        } ?: emptyString()
        }"
    }

    enum class ValidationEntryType {
        Warning,
        Error
    }

    override fun toString() = repr

    val message = repr
}

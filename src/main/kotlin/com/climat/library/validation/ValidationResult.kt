package com.climat.library.validation

import com.climat.library.domain.toolchain.Toolchain
import com.climat.library.parser.exception.getSourceCodeErrorCaretIndicator
import com.climat.library.utils.emptyString
import com.climat.library.utils.newLine
import com.climat.library.validation.validations.ValidationCode
import org.antlr.v4.kotlinruntime.ast.Position
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class ValidationResult(
    private val _message: String,
    private val sourceCode: String,
    private val sourceMap: Position?,
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
        }: $_message ${sourceMap?.let { newLine() + getSourceCodeErrorCaretIndicator(sourceCode, it) } ?: emptyString() }"
    }

    enum class ValidationEntryType {
        Warning,
        Error
    }

    override fun toString() = repr

    val message = repr
}

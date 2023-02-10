package parser.exception

import com.strumenta.kotlinmultiplatform.Math
import newLine
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.ast.Position

internal fun <T : ParserRuleContext, R> T.assertRequire(sourceCode: String, getter: T.() -> R?): R =
    getter(this) ?: throw UnexpectedParsingError("Failed to parse", this, sourceCode)

internal fun ParserRuleContext.throwUnexpected(message: String, sourceCode: String): Nothing =
    throw UnexpectedParsingError(message, this, sourceCode)

internal fun ParserRuleContext.throwExpected(message: String, sourceCode: String): Nothing =
    throw ParsingError(message, this, sourceCode)

// TODO test
internal fun getSourceCodeErrorCaretIndicator(code: String, position: Position): String {
    val codeLines = code.split(newLine()).toMutableList()
    val (start, end) = position
    val startLine = start.line - 1
    val startColumn = start.column
    val endLine = end.line - 1
    val endColumn = end.column

    val lineNumberLength = endLine.toString().length
    val lineNumberSeparator = " |"
    val totalLinePadding = lineNumberLength + lineNumberSeparator.length

    if (startLine < endLine) {
        val startLen = codeLines[startLine].length
        val endLen = codeLines[endLine].length
        codeLines[startLine] += newLine() + " ".repeat(startColumn + totalLinePadding) + "^".repeat(startLen - startColumn)
        codeLines[endLine] += newLine() + " ".repeat(totalLinePadding) + "^".repeat(endColumn)
        (startLine + 1 until endLine).forEach { i ->
            codeLines[i] += newLine() + " ".repeat(totalLinePadding) + "^".repeat(codeLines[i].length)
        }
    } else {
        val len = codeLines[startLine].length
        codeLines[startLine] += newLine() + " ".repeat(startColumn + totalLinePadding) + "^".repeat(endColumn - startColumn + 1)
    }

    val lineViewPortBegin = Math.max(startLine - 2, 0)
    val lineViewPortEnd = Math.min(endLine + 2, codeLines.size - 1)

    val viewPort = codeLines.slice(lineViewPortBegin..lineViewPortEnd).mapIndexed { i, str ->
        val ind = i + lineViewPortBegin + 1
        "${ind.toString().padEnd(lineNumberLength)}$lineNumberSeparator$str"
    }
    val maxLineLength = viewPort.maxOf { it.length } + 3

    return "=".repeat(maxLineLength) +
        newLine() +
        viewPort.joinToString(newLine()) +
        newLine() +
        "=".repeat(maxLineLength)
}

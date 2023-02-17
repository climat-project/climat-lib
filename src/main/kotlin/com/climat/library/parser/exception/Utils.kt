package com.climat.library.parser.exception

import com.climat.library.utils.crossPlatformLineSplit
import com.climat.library.utils.newLine
import com.strumenta.kotlinmultiplatform.Math
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.ast.Position

internal fun <T : ParserRuleContext, R> T.assertRequire(sourceCode: String, getter: T.() -> R?): R =
    getter(this) ?: throw UnexpectedParsingError("Failed to parse", this, sourceCode)

internal fun ParserRuleContext.throwUnexpected(message: String, sourceCode: String): Nothing =
    throw UnexpectedParsingError(message, this, sourceCode)

internal fun ParserRuleContext.throwExpected(message: String, sourceCode: String): Nothing =
    throw ParsingError(message, this, sourceCode)

private fun encase(lines: List<String>, lineNumberingStart: Int, lineSkipStart: Int, lineSkipCount: Int): String {
    val mutLines = lines.toMutableList()
    val hr = "\u2500"
    val t = "\u252C"
    val botT = "\u2534"
    val topRightCorner = "\u2510"
    val bottomRightCorner = "\u2518"
    val lineNumberSeparator = " \u2502"
    val maxLineNumberLength = (lineNumberingStart + mutLines.size - 1 - lineSkipCount).toString().length

    (0 until lineSkipStart).forEach { i ->
        mutLines[i] = (lineNumberingStart + i).toString().padEnd(maxLineNumberLength) + lineNumberSeparator + mutLines[i]
    }
    (lineSkipStart until lineSkipStart + lineSkipCount * 2 step 2).forEach { i ->
        mutLines[i] = (lineNumberingStart + lineSkipStart + (i - lineSkipStart) / 2).toString().padEnd(maxLineNumberLength) + lineNumberSeparator + mutLines[i]
        mutLines[i + 1] = " ".repeat(maxLineNumberLength) + lineNumberSeparator + mutLines[i + 1]
    }
    (lineSkipStart + lineSkipCount * 2 until mutLines.size).forEach { i ->
        mutLines[i] = (lineNumberingStart + i - lineSkipCount).toString().padEnd(maxLineNumberLength) + lineNumberSeparator + mutLines[i]
    }

    val maxLineLength = mutLines.maxOf { it.length }

    (0 until mutLines.size).forEach { i ->
        mutLines[i] = mutLines[i].padEnd(maxLineLength + lineNumberSeparator.length + maxLineNumberLength - 1) + lineNumberSeparator
    }

    val hrTop = hr.repeat(maxLineNumberLength + lineNumberSeparator.length - 1) + t + hr.repeat(maxLineLength) + topRightCorner
    val hrBottom = hr.repeat(maxLineNumberLength + lineNumberSeparator.length - 1) + botT + hr.repeat(maxLineLength) + bottomRightCorner
    return (listOf(hrTop) + mutLines + listOf(hrBottom)).joinToString(newLine())
}

// TODO test
internal fun getSourceCodeErrorCaretIndicator(code: String, position: Position): String {
    val caret = "^"

    val codeLines = code.crossPlatformLineSplit().toMutableList()
    val (start, end) = position
    val startLine = start.line - 1
    val startColumn = start.column
    val endLine = end.line - 1
    val endColumn = end.column

    val lineViewPortBegin = Math.max(startLine - 2, 0)
    val lineViewPortEnd = Math.min(endLine + 2, codeLines.size - 1) + endLine - startLine + 1

    val len = codeLines[startLine].length

    if (startLine < endLine) {
        (
            listOf(caret.repeat(len - startColumn).padStart(len)) +
                codeLines.slice(startLine + 1 until endLine).map { caret.repeat(it.length) } +
                listOf(caret.repeat(endColumn).padStart(endColumn))
            )
            .forEachIndexed { i, str ->
                codeLines.add(startLine - 1 + 2 * (i + 1), str)
            }
    } else {
        codeLines.add(startLine + 1, caret.repeat(endColumn - startColumn + 1).padStart(endColumn + 1))
    }

    val viewPort = codeLines.slice(lineViewPortBegin..lineViewPortEnd)

    return encase(viewPort, lineViewPortBegin + 1, startLine - lineViewPortBegin, endLine - startLine + 1)
}

package com.climat.library.parser.exception

import com.climat.library.emptyString
import com.climat.library.newLine
import org.antlr.v4.kotlinruntime.ParserRuleContext

open class ParsingError(
    message: String,
    ctx: ParserRuleContext,
    sourceCode: String
) : Error("`$message`${newLine()} at ${ctx.position}${newLine()}${ctx.position?.let{ getSourceCodeErrorCaretIndicator(sourceCode, it) } ?: emptyString()}")
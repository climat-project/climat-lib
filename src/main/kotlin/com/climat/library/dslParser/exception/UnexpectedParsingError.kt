package com.climat.library.dslParser.exception

import org.antlr.v4.kotlinruntime.ParserRuleContext

class UnexpectedParsingError(
    message: String,
    ctx: ParserRuleContext,
    sourceCode: String
) : ParsingError(
    "$message. This does not indicate a problem with your code, but a problem with the parser",
    ctx,
    sourceCode
)

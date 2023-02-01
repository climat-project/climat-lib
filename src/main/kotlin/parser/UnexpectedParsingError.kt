package parser

import org.antlr.v4.kotlinruntime.ParserRuleContext

class UnexpectedParsingError(
    message: String,
    ctx: ParserRuleContext
) : ParsingError("$message. This does not indicate a problem with your code, but a problem with the parser", ctx)

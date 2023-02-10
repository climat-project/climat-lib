package parser.exception

import newLine
import org.antlr.v4.kotlinruntime.ParserRuleContext

open class ParsingError(
    message: String,
    ctx: ParserRuleContext // TODO using the position, introduce problematic code, underlined, in the exception message
) : Exception("`$message`${newLine()} at ${ctx.position}")

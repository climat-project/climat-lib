package parser

import org.antlr.v4.kotlinruntime.ParserRuleContext

internal fun <T : ParserRuleContext, R> T.assertRequire(getter: T.() -> R?): R =
    getter(this) ?: throw UnexpectedParsingError("Failed to parse", this)

internal fun ParserRuleContext.throwUnexpected(message: String): Nothing =
    throw UnexpectedParsingError(message, this)

internal fun ParserRuleContext.throwExpected(message: String): Nothing =
    throw ParsingError(message, this)

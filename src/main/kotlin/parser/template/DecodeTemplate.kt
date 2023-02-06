package parser.template

import climat.lang.TemplateLexer
import climat.lang.TemplateParser
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import parser.assertRequire
import parser.errListener
import parser.throwUnexpected

internal fun decodeTemplate(tpl: String): List<IPiece> {
    val lexer = TemplateLexer(CharStreams.fromString(tpl))
    val parser = TemplateParser(CommonTokenStream(lexer))
    parser.addErrorListener(errListener)

    return parser.root().findEntry().map {
        it.findContent()?.let { SimpleString(it.text) }
            ?: it.findInterpolation() ?.let {
                val name = it.assertRequire { IDENTIFIER() }.text
                val mapping = it.findMapping()?.IDENTIFIER()?.text
                val isFlipped = it.NEGATE() != null

                Interpolation(name, mapping, isFlipped)
            } ?: it.throwUnexpected("No content or interpolation found")
    }
}

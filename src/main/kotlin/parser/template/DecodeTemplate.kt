package parser.template

import climat.lang.DslParser
import parser.exception.assertRequire
import parser.exception.throwUnexpected

internal fun decodeTemplate(cliDsl: String, strTpl: DslParser.StrintTemplateContext): Template {
    return Template(
        strTpl.findEntry().map {
            it.findContent()?.let { SimpleString(it.text) }
                ?: it.findInterpolation() ?.let {
                    val name = it.assertRequire(cliDsl) { Interpolation_IDENTIFIER() }.text
                    val mapping = it.findMapping()?.Interpolation_IDENTIFIER()?.text
                    val isFlipped = it.Interpolation_NEGATE() != null

                    Interpolation(name, mapping, isFlipped)
                } ?: it.throwUnexpected("No content or interpolation found", cliDsl)
        }
    )
}

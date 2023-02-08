package parser.template

import climat.lang.DslParser
import parser.assertRequire
import parser.throwUnexpected

internal fun decodeTemplate(strTpl: DslParser.StrintTemplateContext): Template {
    return Template(
        strTpl.findEntry().map {
            it.findContent()?.let { SimpleString(it.text) }
                ?: it.findInterpolation() ?.let {
                    val name = it.assertRequire { Interpolation_IDENTIFIER() }.text
                    val mapping = it.findMapping()?.Interpolation_IDENTIFIER()?.text
                    val isFlipped = it.Interpolation_NEGATE() != null

                    Interpolation(name, mapping, isFlipped)
                } ?: it.throwUnexpected("No content or interpolation found")
        }
    )
}
package com.climat.library.dslParser.template

import climat.lang.DslParser
import com.climat.library.domain.action.template.Interpolation
import com.climat.library.domain.action.template.SimpleString
import com.climat.library.domain.action.template.Template
import com.climat.library.dslParser.exception.assertRequire
import com.climat.library.dslParser.exception.throwUnexpected

internal fun decodeTemplate(cliDsl: String, strTpl: DslParser.StringTemplateContext): Template {
    return Template(
        strTpl.findEntry().map {
            it.findContent()?.let { SimpleString.create(it.text) }
                ?: it.findInterpolation()?.let {
                    val name = it.assertRequire(cliDsl) { Interpolation_IDENTIFIER() }.text
                    val mapping = it.findMapping()?.Interpolation_IDENTIFIER()?.text
                    val isFlipped = it.Interpolation_NEGATE() != null

                    Interpolation(name, mapping, isFlipped)
                } ?: it.throwUnexpected("No content or interpolation found", cliDsl)
        }
    )
}

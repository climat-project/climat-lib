package com.climat.library.dslParser.dsl

import climat.lang.DslParser
import com.climat.library.domain.action.template.SimpleString
import com.climat.library.domain.action.template.Template
import com.climat.library.dslParser.exception.assertRequire
import com.climat.library.dslParser.template.decodeTemplate

internal fun decodeLiteral(cliDsl: String, literal: DslParser.LiteralContext): Template =
    literal
        .findStringTemplate()
        ?.let { decodeTemplate(cliDsl, it) }
        ?: Template(
            listOf(
                SimpleString.create(literal.assertRequire(cliDsl) { findBooleanLiteral() }.text)
            )
        )

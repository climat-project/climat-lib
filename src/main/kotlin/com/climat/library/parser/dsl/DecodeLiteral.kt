package com.climat.library.parser.dsl

import climat.lang.DslParser
import com.climat.library.domain.action.template.SimpleString
import com.climat.library.domain.action.template.Template
import com.climat.library.parser.exception.assertRequire
import com.climat.library.parser.template.decodeTemplate

internal fun decodeLiteral(cliDsl: String, literal: DslParser.LiteralContext): Template =
    literal
        .findStringTemplate()
        ?.let { decodeTemplate(cliDsl, it) }
        ?: Template(listOf(SimpleString(literal.assertRequire(cliDsl) { findBooleanLiteral() }.text)))

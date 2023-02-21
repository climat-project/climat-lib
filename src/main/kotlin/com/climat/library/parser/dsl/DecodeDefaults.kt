package com.climat.library.parser.dsl

import climat.lang.DslParser
import com.climat.library.parser.exception.assertRequire
import com.climat.library.parser.exception.throwExpected
import com.climat.library.utils.filterNotNullValues

internal fun decodeSubDefaults(cliDsl: String, subStatements: List<DslParser.SubStatementsContext>, params: List<DslParser.ParamContext>): Map<String, String> =
    decodeRootDefaults(cliDsl, params) + subStatements.mapNotNull { it.findDefaultOverride() }.associate {
        it.assertRequire(cliDsl) { IDENTIFIER() }.text to
            decodeSimpleString(cliDsl, it.assertRequire(cliDsl) { findLiteral() })
    }

internal fun decodeRootDefaults(cliDsl: String, params: List<DslParser.ParamContext>): Map<String, String> =
    params.associate {
        it.assertRequire(cliDsl) { IDENTIFIER() }.text to it.findLiteral()?.let { decodeSimpleString(cliDsl, it) }
    }.filterNotNullValues()

internal fun decodeSimpleString(cliDsl: String, literal: DslParser.LiteralContext): String {
    val tpl = decodeLiteral(cliDsl, literal)
    if (tpl.refReferences.any()) {
        literal.throwExpected("String interpolation not supported for defaults", cliDsl)
        // TODO maybe we should support it?
    }
    return tpl.str(emptyList())
}

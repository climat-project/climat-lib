package com.climat.library.parser.dsl

import climat.lang.DslParser
import com.climat.library.parser.exception.assertRequire

internal fun decodeAliases(cliDsl: String, statements: List<DslParser.SubModifiersContext>): Array<String> {
    val aliases =
        statements.mapNotNull { it.findAliasesModifier() }.flatMap { it.IDENTIFIER() }.map { it.text } +
            statements.mapNotNull { it.findAliasModifier() }.map { it.assertRequire(cliDsl) { IDENTIFIER() }.text }
    return aliases.toTypedArray()
}

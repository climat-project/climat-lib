package com.climat.library.parser.dsl

import climat.lang.DslParser
import com.climat.library.domain.toolchain.DescendantToolchain
import com.climat.library.parser.docstring.decodeDocstring
import com.climat.library.parser.exception.assertRequire

internal fun decodeSub(cliDsl: String, sub: DslParser.SubContext): DescendantToolchain {
    val statements = sub.assertRequire(cliDsl) { findSubBody() }.findSubStatements()
    val params = sub.findParams()?.findParam().orEmpty()
    val docstring = decodeDocstring(cliDsl, sub.findDocstring())
    val modifiers = sub.findSubModifiers()

    return DescendantToolchain(
        name = sub.assertRequire(cliDsl) { IDENTIFIER() }.text,
        description = docstring.subDoc,
        parameters = decodeParameters(cliDsl, params, docstring.paramDoc),
        action = decodeSubAction(cliDsl, statements),
        children = decodeSubChildren(cliDsl, statements),
        constants = decodeSubConstants(cliDsl, statements),
        allowUnmatched = modifiers.any { it.findRootModifiers()?.MOD_ALLOW_UNMATCHED() != null },
        aliases = decodeAliases(cliDsl, modifiers)
    )
}

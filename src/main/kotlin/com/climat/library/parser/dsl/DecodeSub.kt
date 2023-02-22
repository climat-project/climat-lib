package com.climat.library.parser.dsl

import climat.lang.DslParser
import com.climat.library.domain.toolchain.DescendantToolchain
import com.climat.library.domain.toolchain.DescendantToolchainSourceMap
import com.climat.library.parser.docstring.decodeDocstring
import com.climat.library.parser.exception.assertRequire

internal fun decodeSub(cliDsl: String, sub: DslParser.SubContext): DescendantToolchain {
    val statements = sub.assertRequire(cliDsl) { findSubBody() }.findSubStatements()
    val params = sub.findParams()?.findParam().orEmpty()
    val docstring = decodeDocstring(cliDsl, sub.findDocstring())
    val modifiers = sub.findSubModifiers()

    val name = sub.assertRequire(cliDsl) { IDENTIFIER() }
    val allowUnmatchedMod = modifiers.firstNotNullOfOrNull { it.findRootModifiers()?.MOD_ALLOW_UNMATCHED() }

    return DescendantToolchain(
        name = name.text,
        description = docstring.subDoc,
        parameters = decodeParameters(cliDsl, params, docstring.paramDoc),
        action = decodeSubAction(cliDsl, statements),
        children = decodeSubChildren(cliDsl, statements),
        constants = decodeSubConstants(cliDsl, statements),
        allowUnmatched = allowUnmatchedMod != null,
        aliases = decodeAliases(cliDsl, modifiers),

        sourceMap = DescendantToolchainSourceMap(
            name = name.sourceInterval,
            allowUnmatched = allowUnmatchedMod?.sourceInterval,
        )
    )
}

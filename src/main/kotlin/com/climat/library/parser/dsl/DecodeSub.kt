package com.climat.library.parser.dsl

import climat.lang.DslParser
import com.climat.library.domain.toolchain.DescendantToolchain
import com.climat.library.parser.exception.assertRequire

internal fun decodeSub(cliDsl: String, sub: DslParser.SubContext): DescendantToolchain {
    val (statements, params, docstring, modifiers) = destructureRoot(cliDsl, sub)

    return DescendantToolchain(
        name = sub.assertRequire(cliDsl) { IDENTIFIER() }.text,
        description = docstring.subDoc,
        parameters = decodeParameters(cliDsl, params, docstring.paramDoc),
        parameterDefaults = decodeSubDefaults(cliDsl, statements, params),
        action = decodeSubAction(cliDsl, statements),
        children = decodeSubChildren(cliDsl, statements),
        constants = decodeSubConstants(cliDsl, statements),
        aliases = decodeAliases(cliDsl, modifiers)
    )
}

package com.climat.library.parser.dsl

import climat.lang.DslLexer
import climat.lang.DslParser
import com.climat.library.domain.toolchain.RootToolchain
import com.climat.library.parser.docstring.decodeDocstring
import com.climat.library.parser.exception.CliDslErrorListener
import com.climat.library.parser.exception.assertRequire
import com.climat.library.utils.then
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream

internal fun decodeCliDsl(cliDsl: String): RootToolchain {
    val lexer = DslLexer(CharStreams.fromString(cliDsl))
    val parser = DslParser(CommonTokenStream(lexer))
    parser.addErrorListener(CliDslErrorListener(cliDsl))

    val root = parser.root()

    val (statements, params, docstring) = destructureRoot(cliDsl, root)

    return RootToolchain(
        name = root.assertRequire(cliDsl) { IDENTIFIER() }.text,
        description = docstring.subDoc,
        parameters = decodeParameters(cliDsl, params, docstring.paramDoc),
        parameterDefaults = decodeRootDefaults(cliDsl, params),
        action = decodeRootAction(cliDsl, statements),
        children = decodeRootChildren(cliDsl, statements),
        constants = decodeRootConstants(cliDsl, statements),
        resources = emptyArray()
    )
}

internal fun destructureRoot(cliDsl: String, sub: DslParser.SubContext) =
    sub.assertRequire(cliDsl) { findSubBody() }.findSubStatements() then
        sub.findParams()?.findParam().orEmpty() then
        decodeDocstring(cliDsl, sub.findDocstring()) then
        sub.findSubModifiers()

internal fun destructureRoot(cliDsl: String, root: DslParser.RootContext) =
    root.assertRequire(cliDsl) { findRootBody() }.findRootStatements() then
        root.findParams()?.findParam().orEmpty() then
        decodeDocstring(cliDsl, root.findDocstring())

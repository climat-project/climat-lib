package com.climat.library.parser.dsl

import climat.lang.DslParser
import com.climat.library.domain.ref.Constant
import com.climat.library.domain.ref.Ref
import com.climat.library.parser.exception.assertRequire

internal fun decodeSubConstants(cliDsl: String, statements: List<DslParser.SubStatementsContext>): Array<Constant> =
    decodeRootConstants(cliDsl, statements.mapNotNull { it.findRootStatements() })

internal fun decodeRootConstants(cliDsl: String, statements: List<DslParser.RootStatementsContext>): Array<Constant> =
    statements.mapNotNull { it.findConstDef() }
        .map { context ->
            context.assertRequire(cliDsl) { findLiteral() }.let { literal ->
                Constant(
                    name = context.assertRequire(cliDsl) { IDENTIFIER() }.text,
                    type = if (literal.findStringTemplate() != null) Ref.Type.Arg else Ref.Type.Flag,
                    value = decodeLiteral(cliDsl, literal)
                )
            }
        }.toTypedArray()

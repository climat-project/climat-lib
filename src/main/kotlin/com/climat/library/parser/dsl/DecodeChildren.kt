package com.climat.library.parser.dsl

import climat.lang.DslParser
import com.climat.library.domain.toolchain.DescendantToolchain

fun decodeSubChildren(cliDsl: String, statements: List<DslParser.SubStatementsContext>): Array<DescendantToolchain> =
    decodeRootChildren(cliDsl, statements.mapNotNull { it.findRootStatements() })

fun decodeRootChildren(cliDsl: String, statements: List<DslParser.RootStatementsContext>): Array<DescendantToolchain> =
    statements.mapNotNull { it.findSub() }
        .map { decodeSub(cliDsl, it) }
        .toTypedArray()

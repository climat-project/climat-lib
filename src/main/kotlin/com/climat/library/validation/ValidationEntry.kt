package com.climat.library.validation

import org.antlr.v4.kotlinruntime.ast.Position

internal class ValidationEntry(
    val message: String,
    val sourceMap: Position? = null
)

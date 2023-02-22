package com.climat.library.domain

import org.antlr.v4.kotlinruntime.ast.Position

abstract class SourceTraceable {
    internal abstract val sourceMap: Position?
}

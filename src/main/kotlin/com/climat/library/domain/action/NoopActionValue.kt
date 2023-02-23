package com.climat.library.domain.action

import org.antlr.v4.kotlinruntime.ast.Position
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class NoopActionValue internal constructor() : ActionValueBase<Nothing>() {
    override val sourceMap: Position?
        get() = null
}

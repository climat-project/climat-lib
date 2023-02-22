package com.climat.library.domain.action

import org.antlr.v4.kotlinruntime.misc.Interval
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class NoopActionValue internal constructor() : ActionValueBase<Nothing>() {
    override val sourceMap: Interval?
        get() = null
}

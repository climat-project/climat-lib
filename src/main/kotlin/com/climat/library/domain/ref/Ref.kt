package com.climat.library.domain.ref

import org.antlr.v4.kotlinruntime.misc.Interval
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
abstract class Ref {
    abstract val name: String
    abstract val type: Type
    internal abstract val sourceMap: Interval

    enum class Type {
        Flag,
        Arg
    }
}

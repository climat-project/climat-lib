package com.climat.library.domain.ref

import com.climat.library.domain.SourceTraceable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
abstract class Ref : SourceTraceable() {
    abstract val name: String
    abstract val type: Type

    enum class Type {
        Flag,
        Arg
    }
}

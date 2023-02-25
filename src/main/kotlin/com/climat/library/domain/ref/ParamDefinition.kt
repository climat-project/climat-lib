package com.climat.library.domain.ref

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
abstract class ParamDefinition : Ref() {
    abstract val shorthand: String?
    abstract val description: String
    abstract val optional: Boolean
    abstract val default: String?
}

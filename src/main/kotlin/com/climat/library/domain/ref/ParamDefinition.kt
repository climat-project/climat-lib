package com.climat.library.domain.ref

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class ParamDefinition internal constructor(
    override val name: String,
    override val type: Type,
    val optional: Boolean,
    val shorthand: String?,
    val description: String,
    val default: String?
) : Ref()

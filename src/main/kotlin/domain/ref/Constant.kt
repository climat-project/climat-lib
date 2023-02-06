package domain.ref

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class Constant(
    override val name: String,
    override val type: Type,
    val value: String
) : Ref()

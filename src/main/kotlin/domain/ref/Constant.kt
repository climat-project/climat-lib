package domain.ref

import parser.template.Template
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class Constant internal constructor(
    override val name: String,
    override val type: Type,
    internal val value: Template
) : Ref()

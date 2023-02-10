package domain.action

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class NoopActionValue internal constructor() : ActionValueBase<Nothing>()

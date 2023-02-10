package jsExportable

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class JsExportablePair<TKey, TValue>(
    val key: TKey,
    val value: TValue
) {
    internal constructor(from: Pair<TKey, TValue>) : this(from.first, from.second)
}

package com.climat.library.jsExportable

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class JsExportableMap<TKey, TValue>(private val from: Map<TKey, TValue>) {
    val entries: Array<JsExportablePair<TKey, TValue>>
        get() = from.entries.map { (key, value) -> JsExportablePair(key, value) }.toTypedArray()

    fun get(key: TKey): TValue? = from[key]

    fun isEmpty(): Boolean = from.isEmpty()
}

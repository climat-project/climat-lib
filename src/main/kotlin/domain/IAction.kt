@file:OptIn(ExperimentalJsExport::class)

package domain

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
interface IAction {
    val template: String
}
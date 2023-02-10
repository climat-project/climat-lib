package parser.template

import domain.ref.RefWithValue
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
interface IPiece {
    fun str(values: Collection<RefWithValue>): String
}

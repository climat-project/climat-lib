package parser.template

import domain.ref.Ref
import domain.ref.RefWithValue
import emptyString
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
class Interpolation(
    val name: String,
    val mapping: String?,
    val isFlipped: Boolean
) : IPiece {
    override fun str(values: Collection<RefWithValue>): String {
        val value = values.find { it.ref.name == name }!!
        return if (mapping != null) {
            when (value.ref.type) {
                Ref.Type.Flag -> if (value.value.toBooleanStrict()) {
                    mapping
                } else {
                    emptyString()
                }
                Ref.Type.Arg -> if (value.value.isNotEmpty()) {
                    "$mapping=${value.value}"
                } else {
                    emptyString()
                }
            }
        } else {
            value.value
        }
    }
}

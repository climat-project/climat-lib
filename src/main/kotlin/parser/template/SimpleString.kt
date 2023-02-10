package parser.template

import domain.ref.RefWithValue

internal data class SimpleString(val value: String) : IPiece {
    override fun str(values: Collection<RefWithValue>) = value
}

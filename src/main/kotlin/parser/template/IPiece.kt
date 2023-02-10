package parser.template

import domain.ref.RefWithValue

internal interface IPiece {
    fun str(values: Collection<RefWithValue>): String
}

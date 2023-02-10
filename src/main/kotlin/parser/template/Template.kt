package parser.template

import domain.ref.RefWithValue
import emptyString

internal data class Template(
    private val pieces: List<IPiece>,
) {
    fun str(values: Collection<RefWithValue>): String =
        pieces.joinToString(emptyString()) {
            it.str(values)
        }
            // Remove duplicate whitespaces
            .replace("\\s+".toRegex(), " ")

    val refReferences: List<Interpolation>
        get() = pieces.filterIsInstance<Interpolation>()
}

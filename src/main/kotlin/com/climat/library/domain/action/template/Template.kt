package com.climat.library.domain.action.template

import com.climat.library.domain.ref.RefWithAnyValue
import com.climat.library.utils.emptyString

internal data class Template(
    private val pieces: List<IPiece>,
) {
    fun str(values: Collection<RefWithAnyValue>): String =
        pieces.joinToString(emptyString()) {
            it.str(values)
        }
            // Remove duplicate whitespaces
            .replace("\\s+".toRegex(), " ")

    val refReferences: List<Interpolation>
        get() = pieces.filterIsInstance<Interpolation>()
}

package com.climat.library.parser.template

import com.climat.library.domain.ref.RefWithValue

internal interface IPiece {
    fun str(values: Collection<RefWithValue>): String
}

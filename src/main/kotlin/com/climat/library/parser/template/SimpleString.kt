package com.climat.library.parser.template

import com.climat.library.domain.ref.RefWithValue

internal data class SimpleString(val value: String) : IPiece {
    override fun str(values: Collection<RefWithValue>) = value
}

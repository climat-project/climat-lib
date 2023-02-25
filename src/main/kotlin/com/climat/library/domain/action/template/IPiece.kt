package com.climat.library.domain.action.template

import com.climat.library.domain.ref.RefWithAnyValue

internal interface IPiece {
    fun str(values: Collection<RefWithAnyValue>): String
}

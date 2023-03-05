package com.climat.library.domain.action.template

import com.climat.library.domain.ref.RefWithAnyValue

internal class SimpleString private constructor(val value: String) : IPiece {

    companion object {
        fun create(value: String) = SimpleString(
            value.replace(
                Regex("\\\\(\\$|\"|\\\\)")
            ) { it.groups[1]!!.value }
        )
    }

    override fun str(values: Collection<RefWithAnyValue>) = value
}

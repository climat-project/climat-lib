package com.climat.library.parser.template

import com.climat.library.domain.ref.Ref
import com.climat.library.domain.ref.RefWithValue
import com.climat.library.emptyString

internal class Interpolation(
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

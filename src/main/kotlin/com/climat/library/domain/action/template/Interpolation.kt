package com.climat.library.domain.action.template

import com.climat.library.domain.ref.ArgDefinition
import com.climat.library.domain.ref.Constant
import com.climat.library.domain.ref.FlagDefinition
import com.climat.library.domain.ref.PredefinedParamDefinition
import com.climat.library.domain.ref.RefWithValue
import com.climat.library.utils.emptyString

internal class Interpolation(
    val name: String,
    val mapping: String?,
    val isFlipped: Boolean
) : IPiece {
    override fun str(values: Collection<RefWithValue>): String {
        val refWithValue = values.find { it.ref.name == name }!!
        val value = refWithValue.value
        return if (mapping != null) {
            when (val ref = refWithValue.ref) {
                is FlagDefinition -> mapBoolean(value, mapping)
                is ArgDefinition -> mapString(value, mapping)
                is Constant -> if (ref.isBoolean) {
                    mapBoolean(value, mapping)
                } else {
                    mapString(value, mapping)
                }

                is PredefinedParamDefinition -> throw Exception("Cannot map vararg argument type") // TODO proper error
                else -> throw Exception("Type type not supported") // TODO proper error
            }
        } else {
            value
        }
    }

    private fun mapString(value: String, mapping: String): String = if (value.isNotEmpty()) {
        "$mapping=$value"
    } else {
        emptyString()
    }

    private fun mapBoolean(value: String, mapping: String): String = if (value.toBooleanStrict() xor isFlipped) {
        mapping
    } else {
        emptyString()
    }
}

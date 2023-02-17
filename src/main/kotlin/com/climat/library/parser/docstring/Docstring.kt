package com.climat.library.parser.docstring

import com.climat.library.utils.emptyString

internal data class Docstring(
    val subDoc: String,
    val paramDoc: Map<String, String>
) {

    companion object {
        val empty: Docstring
            get() = Docstring(emptyString(), emptyMap())
    }
}

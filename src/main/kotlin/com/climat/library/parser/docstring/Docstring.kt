package com.climat.library.parser.docstring

import com.climat.library.emptyString

internal data class Docstring(
    val functionDoc: String,
    val paramDoc: Map<String, String>
) {

    companion object {
        val empty: Docstring
            get() = Docstring(emptyString(), emptyMap())
    }
}
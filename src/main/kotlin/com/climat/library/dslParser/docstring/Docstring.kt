package com.climat.library.dslParser.docstring

import com.climat.library.utils.emptyString

internal class Docstring(
    val subDoc: String,
    val paramDoc: Map<String, String>
) {

    companion object {
        val empty: Docstring
            get() = Docstring(emptyString(), emptyMap())
    }
}

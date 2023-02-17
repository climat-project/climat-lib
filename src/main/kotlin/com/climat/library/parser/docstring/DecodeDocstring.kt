package com.climat.library.parser.docstring

import climat.lang.DslParser
import com.climat.library.parser.exception.assertRequire
import com.climat.library.utils.emptyString

internal fun decodeDocstring(cliDsl: String, docstring: DslParser.DocstringContext?): Docstring {
    if (docstring == null) {
        return Docstring.empty
    }

    val entries = docstring.findDocstringEntry()
    if (entries.isEmpty()) {
        return Docstring.empty
    }
    val first = entries.first()

    return first.Docstring_CONTENT().let {
        if (it != null) {
            Docstring(
                it.text.trim(),
                pair(cliDsl, entries.drop(1))
            )
        } else {
            Docstring(emptyString(), pair(cliDsl, entries))
        }
    }
}

private fun pair(cliDsl: String, docs: List<DslParser.DocstringEntryContext>) =
    docs.chunked(2)
        .map { (tag, doc) -> tag.assertRequire(cliDsl) { findParamTag() } to doc.assertRequire(cliDsl) { Docstring_CONTENT() } }
        .associate { (tag, doc) -> tag.assertRequire(cliDsl) { Docstring_IDENTIFIER() }.text.trim() to doc.text.trim() }

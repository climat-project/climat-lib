package parser.docstring

import climat.lang.DslParser
import emptyString
import parser.exception.assertRequire

internal fun decodeDocstring(docstring: DslParser.DocstringContext?): Docstring {
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
                pair(entries.drop(1))
            )
        } else {
            Docstring(emptyString(), pair(entries))
        }
    }
}

private fun pair(docs: List<DslParser.DocstringEntryContext>) =
    docs.chunked(2)
        .map { (tag, doc) -> tag.assertRequire { findParamTag() } to doc.assertRequire { Docstring_CONTENT() } }
        .associate { (tag, doc) -> tag.assertRequire { Docstring_IDENTIFIER() }.text.trim() to doc.text.trim() }

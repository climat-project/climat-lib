package parser

import climat.lang.DslParser
import emptyString

internal data class Docstring(
    val functionDoc: String,
    val paramDoc: Map<String, String>
) {
    companion object {
        val empty: Docstring
            get() = Docstring(emptyString(), emptyMap())
    }
}

internal fun decodeDocstring(docstring: DslParser.DocstringContext?): Docstring {
    if (docstring == null) {
        return Docstring.empty
    }

    // TODO add warnings
    // parser.addErrorListener(errListener)
    val entries = docstring.findDocstringEntry()
    if (entries.isEmpty()) {
        return Docstring.empty
    }
    val first = entries.first()

    return first.Docstring_CONTENT().let {
        if (it != null) {
            Docstring(
                it.text,
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
        .associate { (tag, doc) -> tag.assertRequire { Docstring_IDENTIFIER() }.text to doc.text }

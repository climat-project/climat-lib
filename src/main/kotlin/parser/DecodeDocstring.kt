package parser

import climat.lang.DocstringLexer
import climat.lang.DocstringParser
import emptyString
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream

internal data class Docstring(
    val functionDoc: String,
    val paramDoc: Map<String, String>
) {
    companion object {
        val empty: Docstring
            get() = Docstring(emptyString(), emptyMap())
    }
}

internal fun decodeDocstring(docstring: String): Docstring {
    val lexer = DocstringLexer(CharStreams.fromString(docstring))
    val parser = DocstringParser(CommonTokenStream(lexer))

    // TODO add warnings
    // parser.addErrorListener(errListener)
    val entries = parser.root().findEntry()
    if (entries.isEmpty()) {
        return Docstring.empty
    }
    val first = entries.first()

    return first.CONTENT().let {
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

private fun pair(docs: List<DocstringParser.EntryContext>) =
    docs.chunked(2)
        .map { (tag, doc) -> tag.assertRequire { findParamTag() } to doc.assertRequire { CONTENT() } }
        .associate { (tag, doc) -> tag.assertRequire { IDENTIFIER() }.text to doc.text }

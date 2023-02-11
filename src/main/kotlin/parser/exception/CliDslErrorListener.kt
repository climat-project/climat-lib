package parser.exception

import com.strumenta.kotlinmultiplatform.BitSet
import newLine
import org.antlr.v4.kotlinruntime.ANTLRErrorListener
import org.antlr.v4.kotlinruntime.Parser
import org.antlr.v4.kotlinruntime.RecognitionException
import org.antlr.v4.kotlinruntime.Recognizer
import org.antlr.v4.kotlinruntime.ast.Point
import org.antlr.v4.kotlinruntime.ast.Position
import org.antlr.v4.kotlinruntime.atn.ATNConfigSet
import org.antlr.v4.kotlinruntime.dfa.DFA

internal class CliDslErrorListener(private val sourceCode: String) : ANTLRErrorListener {
    override fun reportAmbiguity(
        recognizer: Parser,
        dfa: DFA,
        startIndex: Int,
        stopIndex: Int,
        exact: Boolean,
        ambigAlts: BitSet,
        configs: ATNConfigSet
    ) {
        TODO("Not yet implemented")
    }

    override fun reportAttemptingFullContext(
        recognizer: Parser,
        dfa: DFA,
        startIndex: Int,
        stopIndex: Int,
        conflictingAlts: BitSet,
        configs: ATNConfigSet
    ) {
        TODO("Not yet implemented")
    }

    override fun reportContextSensitivity(
        recognizer: Parser,
        dfa: DFA,
        startIndex: Int,
        stopIndex: Int,
        prediction: Int,
        configs: ATNConfigSet
    ) {
        TODO("Not yet implemented")
    }

    override fun syntaxError(
        recognizer: Recognizer<*, *>,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String,
        e: RecognitionException?
    ) {
        throw Exception(
            "$msg, at line: $line, col: $charPositionInLine${newLine()}${getSourceCodeErrorCaretIndicator(
                sourceCode,
                Position(
                    Point(line, charPositionInLine),
                    Point(line, charPositionInLine)
                )
            )}"
        )
    }
}

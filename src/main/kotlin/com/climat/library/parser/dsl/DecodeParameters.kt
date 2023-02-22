package com.climat.library.parser.dsl

import climat.lang.DslParser
import com.climat.library.domain.ref.ParamDefinition
import com.climat.library.domain.ref.Ref
import com.climat.library.parser.exception.assertRequire
import com.climat.library.parser.exception.throwExpected
import com.climat.library.parser.exception.throwUnexpected
import com.climat.library.utils.emptyString

internal fun decodeParameters(cliDsl: String, params: List<DslParser.ParamContext>, paramDescriptions: Map<String, String>): Array<ParamDefinition> =
    params.map { parsedParam ->
        val paramName = parsedParam.assertRequire(cliDsl) { IDENTIFIER() }.text

        val (paramType, optional) = parsedParam.assertRequire(cliDsl) { findParamType() }.let {
            when {
                it.FLAG() != null -> Ref.Type.Flag to true
                it.findArgument() != null -> Ref.Type.Arg to (it.findArgument()!!.QMARK() != null)
                else -> it.throwUnexpected("Could not parse parameter type", cliDsl)
            }
        }
        ParamDefinition(
            name = parsedParam.assertRequire(cliDsl) { IDENTIFIER() }.text,
            description = paramDescriptions[paramName] ?: emptyString(),
            optional = optional,
            shorthand = parsedParam.findParamShort()?.text,
            type = paramType,
            default = parsedParam.findLiteral()?.let { decodeSimpleString(cliDsl, it) },
            sourceMap = parsedParam.sourceInterval
        )
    }.toTypedArray()

internal fun decodeSimpleString(cliDsl: String, literal: DslParser.LiteralContext): String {
    val tpl = decodeLiteral(cliDsl, literal)
    if (tpl.refReferences.any()) {
        literal.throwExpected("String interpolation not supported for defaults", cliDsl)
        // TODO maybe we should support it?
    }
    return tpl.str(emptyList())
}

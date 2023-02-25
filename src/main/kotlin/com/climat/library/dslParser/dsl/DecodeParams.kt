package com.climat.library.dslParser.dsl

import climat.lang.DslParser
import com.climat.library.domain.ref.ArgDefinition
import com.climat.library.domain.ref.FlagDefinition
import com.climat.library.domain.ref.ParamDefinition
import com.climat.library.dslParser.exception.assertRequire
import com.climat.library.dslParser.exception.throwExpected
import com.climat.library.dslParser.exception.throwUnexpected
import com.climat.library.utils.emptyString

internal fun decodeParams(
    cliDsl: String,
    params: List<DslParser.ParamContext>,
    paramDescriptions: Map<String, String>
): Array<ParamDefinition> =
    params.map { parsedParam ->
        val paramName = parsedParam.assertRequire(cliDsl) { IDENTIFIER() }.text
        val description = paramDescriptions[paramName] ?: emptyString()
        val shorthand = parsedParam.findParamShort()?.text
        parsedParam.assertRequire(cliDsl) { findParamType() }.let {
            when {
                it.FLAG() != null -> {
                    FlagDefinition(
                        name = paramName,
                        shorthand = shorthand,
                        description = description,
                        sourceMap = parsedParam.position!!
                    )
                }

                it.findArgument() != null -> {
                    val arg = it.findArgument()!!
                    ArgDefinition(
                        name = paramName,
                        shorthand = shorthand,
                        description = description,
                        optional = (arg.QMARK() != null),
                        default = arg.findLiteral()?.let { decodeSimpleString(cliDsl, it) },
                        sourceMap = parsedParam.position!!,
                    )
                }

                else -> it.throwUnexpected("Could not parse parameter type", cliDsl)
            }
        }
    }.toTypedArray()

internal fun decodeSimpleString(cliDsl: String, literal: DslParser.LiteralContext): String {
    val tpl = decodeLiteral(cliDsl, literal)
    if (tpl.refReferences.any()) {
        literal.throwExpected("String interpolation not supported for defaults", cliDsl)
        // TODO maybe we should support it?
    }
    return tpl.str(emptyList())
}

package parser

import climat.lang.CliDslLexer
import climat.lang.CliDslParser
import domain.action.ActionValueBase
import domain.action.ScopeParamsActionValue
import domain.action.TemplateActionValue
import domain.ref.Constant
import domain.ref.ParamDefinition
import domain.ref.Ref
import domain.toolchain.DescendantToolchain
import domain.toolchain.RootToolchain
import emptyString
import filterNotNullValues
import noopAction
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream

internal fun decodeCliDsl(cliDsl: String): RootToolchain {
    val lexer = CliDslLexer(CharStreams.fromString(cliDsl))
    val parser = CliDslParser(CommonTokenStream(lexer))
    parser.addErrorListener(errListener)

    val func = parser.root().assertRequire { findFunc() }

    val (statements, params, docstring) = destructureFunc(func)

    return RootToolchain(
        name = func.assertRequire { IDENTIFIER() }.text,
        description = decodeFunctionDescription(docstring),
        parameters = decodeParameters(params, getParamDescriptionMap(docstring)),
        parameterDefaults = decodeDefaults(statements, params),
        action = decodeAction(statements),
        children = decodeChildren(statements),
        constants = decodeConstants(statements),
        resources = emptyArray()
    )
}

private fun decodeDescendantToolchain(func: CliDslParser.FuncContext): DescendantToolchain {
    val (statements, params, docstring) = destructureFunc(func)

    return DescendantToolchain(
        name = func.assertRequire { IDENTIFIER() }.text,
        description = decodeFunctionDescription(docstring),
        parameters = decodeParameters(params, getParamDescriptionMap(docstring)),
        parameterDefaults = decodeDefaults(statements, params),
        action = decodeAction(statements),
        children = decodeChildren(statements),
        constants = decodeConstants(statements),
        aliases = decodeAliases(statements)
    )
}

private fun decodeAliases(statements: List<CliDslParser.FuncStatementsContext>): Array<String> {
    val aliasProps = statements.mapNotNull { it.findAliases() }
        .toList()
    if (aliasProps.size >= 2) {
        aliasProps[1].throwExpected("More than one aliases property not allowed")
    }
    if (aliasProps.size == 1) {
        val alias = aliasProps.first()
        return alias.IDENTIFIER().map { it.text }.toTypedArray()
    }
    return emptyArray()
}

private fun decodeAction(statements: List<CliDslParser.FuncStatementsContext>): ActionValueBase<*> {
    val actions = statements.mapNotNull { it.findAction() }
        .toList()
    if (actions.size >= 2) {
        actions[1].throwExpected("More than one action property is not allowed")
    }
    if (actions.size == 1) {
        val child = actions.first()
        return child.assertRequire { findActionValue() }.let {
            it.findStringLiteral()?.let(::getLiteralTextFromStringContext)?.let(::TemplateActionValue)
                ?: it.assertRequire { SCOPE_PARAMS() }.text.let { ScopeParamsActionValue() }
        }
    }
    return noopAction()
}

private fun destructureFunc(func: CliDslParser.FuncContext) =
    Triple(
        func.assertRequire { findFuncBody() }.findFuncStatements(),
        func.findParams()?.findParam().orEmpty(),
        func.findDocstring()
    )

private fun decodeDefaults(funcStatements: List<CliDslParser.FuncStatementsContext>, params: List<CliDslParser.ParamContext>): Map<String, String> =
    params.associate {
        it.assertRequire { IDENTIFIER() }.text to it.findLiteral()?.let(::getLiteralText)
    }.filterNotNullValues() + funcStatements.mapNotNull { it.findDefaultOverride() }.associate {
        it.assertRequire { IDENTIFIER() }.text to
            it.assertRequire { findLiteral() }.let(::getLiteralText)
    }

private fun decodeConstants(statements: List<CliDslParser.FuncStatementsContext>): Array<Constant> =
    statements.mapNotNull { it.findConstDef() }
        .map { context ->
            context.assertRequire { findLiteral() }.let { literal ->
                Constant(
                    name = context.assertRequire { IDENTIFIER() }.text,
                    type = if (literal.findStringLiteral() != null) Ref.Type.Arg else Ref.Type.Flag,
                    value = getLiteralText(literal)
                )
            }
        }.toTypedArray()

private fun getLiteralText(literal: CliDslParser.LiteralContext): String =
    literal
        .findStringLiteral()
        ?.let(::getLiteralTextFromStringContext)
        ?: literal.assertRequire { findBooleanLiteral() }.text

private fun getLiteralTextFromStringContext(literal: CliDslParser.StringLiteralContext): String? =
    literal
        .STRING_LITERAL()
        ?.text
        ?.drop(1)
        ?.dropLast(1)
        ?.replace("\\\"", "\"")

private fun decodeChildren(statements: List<CliDslParser.FuncStatementsContext>): Array<DescendantToolchain> {
    val children = statements.mapNotNull { it.findChildren() }
        .toList()
    if (children.size >= 2) {
        children[1].throwExpected("More than one child property not allowed")
    }
    if (children.size == 1) {
        val child = children.first()
        return child.findFunc().map(::decodeDescendantToolchain).toTypedArray()
    }
    return emptyArray()
}

private fun decodeParameters(params: List<CliDslParser.ParamContext>, paramDescriptions: Map<String, String>): Array<ParamDefinition> =
    params.map { parsedParam ->
        val paramName = parsedParam.assertRequire { IDENTIFIER() }.text

        ParamDefinition(
            name = parsedParam.assertRequire { IDENTIFIER() }.text,
            description = paramDescriptions[paramName] ?: emptyString(),
            optional = parsedParam.QMARK() != null,
            shorthand = parsedParam.ALPHANUMERIC()?.text,
            type = parsedParam.assertRequire { findParamType() }.let {
                when {
                    it.FLAG() != null -> Ref.Type.Flag
                    it.ARGUMENT() != null -> Ref.Type.Arg
                    else -> it.throwUnexpected("Could not parse parameter type")
                }
            }
        )
    }.toTypedArray()

fun getParamDescriptionMap(docstring: CliDslParser.DocstringContext?): Map<String, String> =
    docstring?.findParamDescription().orEmpty()
        .associate { it.assertRequire { IDENTIFIER() }.text to it.assertRequire { findDescription() }.text }

private fun decodeFunctionDescription(docstring: CliDslParser.DocstringContext?) =
    docstring?.findFunctionDescription()?.text ?: emptyString()

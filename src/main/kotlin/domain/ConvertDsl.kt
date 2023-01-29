package domain

import climat.lang.CliDslLexer
import climat.lang.CliDslParser
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

internal fun decodeFromString(cliDsl: String): RootToolchain {
    val lexer = CliDslLexer(CharStreams.fromString(cliDsl))
    val parser = CliDslParser(CommonTokenStream(lexer))
    val func = parser.func()

    val (funcStatements, funcParams) = destructureFunc(func)

    return RootToolchain(
        name = func.NAME()?.text ?: throw Exception("Missing function name"),
        description = emptyString(), // TODO implement
        parameters = decodeParameters(funcParams),
        parameterDefaults = decodeDefaults(funcParams),
        action = decodeAction(funcStatements),
        children = decodeChildren(funcStatements),
        constants = decodeConstants(funcStatements),
        resources = emptyArray()
    )
}

private fun decodeDescendantToolchain(func: CliDslParser.FuncContext): DescendantToolchain {
    val (funcStatements, funcParams) = destructureFunc(func)

    return DescendantToolchain(
        name = func.NAME()?.text ?: throw Exception("Missing function name"),
        description = emptyString(), // TODO implement
        parameters = decodeParameters(funcParams),
        parameterDefaults = decodeDefaults(funcParams),
        action = decodeAction(funcStatements),
        children = decodeChildren(funcStatements),
        constants = decodeConstants(funcStatements),
    )
}

fun decodeAction(statements: List<CliDslParser.FuncStatementsContext>): IAction {
    val actions = statements.mapNotNull { it.findAction() }
        .toList()
    if (actions.size >= 2) {
        throw Exception("More than one child property not allowed")
    }
    if (actions.size == 1) {
        val child = actions.first()
        val text = child.findStringLiteral()?.let(::getLiteralTextFromStringContext) ?: throw Exception("No action string provided!")

        return object : IAction {
            override val template: String
                get() = text
        }
    }
    return noopAction()
}

private fun destructureFunc(func: CliDslParser.FuncContext) =
    (func.findFuncBody()?.findFuncStatements() ?: throw Exception("Function body not found")) to
        func.findParams()?.findParam().orEmpty()

private fun decodeDefaults(params: List<CliDslParser.ParamContext>): Map<String, String> =
    params.associate {
        (it.NAME()?.text ?: throw Exception()) to it.findLiteral()?.let(::getLiteralText)
    }.filterNotNullValues()

private fun decodeConstants(statements: List<CliDslParser.FuncStatementsContext>): Array<Constant> =
    statements.mapNotNull { it.findConstDef() }
        .map { context ->
            context.findLiteral()?.let { literal ->
                Constant(
                    name = context.NAME()?.text ?: throw Exception("Missing constant name"),
                    type = if (literal.findStringLiteral() != null) Ref.Type.Arg else Ref.Type.Flag,
                    value = getLiteralText(literal)
                )
            } ?: throw Exception("Constant value not found")
        }.toTypedArray()

private fun getLiteralText(literal: CliDslParser.LiteralContext) =
    literal
        .findStringLiteral()
        ?.let(::getLiteralTextFromStringContext)
        ?: literal.findBooleanLiteral()?.text
        ?: throw Exception("Could not parse literal text")

private fun getLiteralTextFromStringContext(literal: CliDslParser.StringLiteralContext) =
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
        throw Exception("More than one child property not allowed")
    }
    if (children.size == 1) {
        val child = children.first()
        return child.findFunc().map(::decodeDescendantToolchain).toTypedArray()
    }
    return emptyArray()
}

private fun decodeParameters(params: List<CliDslParser.ParamContext>): Array<ParamDefinition> =
    params.map { parsedParam ->
        ParamDefinition(
            name = parsedParam.NAME()?.text ?: throw Exception("Parameter name missing"),
            description = emptyString(), // TODO implement
            optional = parsedParam.QMARK() != null,
            shorthand = parsedParam.SHORTHAND()?.text,
            type = parsedParam.findParamType()?.let {
                when {
                    it.FLAG() != null -> Ref.Type.Flag
                    it.ARGUMENT() != null -> Ref.Type.Arg
                    else -> throw Exception("Could not parse parameter type")
                }
            } ?: throw Exception("Parameter type missing")
        )
    }.toTypedArray()

package parser.dsl

import climat.lang.DslLexer
import climat.lang.DslParser
import domain.action.ActionValueBase
import domain.action.CustomScriptActionValue
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
import parser.docstring.decodeDocstring
import parser.exception.CliDslErrorListener
import parser.exception.assertRequire
import parser.exception.throwExpected
import parser.exception.throwUnexpected
import parser.template.SimpleString
import parser.template.Template
import parser.template.decodeTemplate

internal fun decodeCliDsl(cliDsl: String): RootToolchain {
    val lexer = DslLexer(CharStreams.fromString(cliDsl))
    val parser = DslParser(CommonTokenStream(lexer))
    parser.addErrorListener(CliDslErrorListener(cliDsl))

    val func = parser.root().assertRequire(cliDsl) { findFunc() }

    val (statements, params, docstring) = destructureFunc(cliDsl, func)

    return RootToolchain(
        name = func.assertRequire(cliDsl) { IDENTIFIER() }.text,
        description = docstring.functionDoc,
        parameters = decodeParameters(cliDsl, params, docstring.paramDoc),
        parameterDefaults = decodeDefaults(cliDsl, statements, params),
        action = decodeAction(cliDsl, statements),
        children = decodeChildren(cliDsl, statements),
        constants = decodeConstants(cliDsl, statements),
        resources = emptyArray()
    )
}

private fun decodeDescendantToolchain(cliDsl: String, func: DslParser.FuncContext): DescendantToolchain {
    val (statements, params, docstring) = destructureFunc(cliDsl, func)

    return DescendantToolchain(
        name = func.assertRequire(cliDsl) { IDENTIFIER() }.text,
        description = docstring.functionDoc,
        parameters = decodeParameters(cliDsl, params, docstring.paramDoc),
        parameterDefaults = decodeDefaults(cliDsl, statements, params),
        action = decodeAction(cliDsl, statements),
        children = decodeChildren(cliDsl, statements),
        constants = decodeConstants(cliDsl, statements),
        aliases = decodeAliases(cliDsl, statements)
    )
}

private fun decodeAliases(cliDsl: String, statements: List<DslParser.FuncStatementsContext>): Array<String> {
    val aliasProps = statements.mapNotNull { it.findAliases() }
        .toList()
    if (aliasProps.size >= 2) {
        aliasProps[1].throwExpected("More than one aliases property not allowed", cliDsl)
    }
    if (aliasProps.size == 1) {
        val alias = aliasProps.first()
        return alias.IDENTIFIER().map { it.text }.toTypedArray()
    }
    return emptyArray()
}

private fun decodeAction(cliDsl: String, statements: List<DslParser.FuncStatementsContext>): ActionValueBase<*> {
    val actions = statements.mapNotNull { it.findAction() }
        .toList()
    if (actions.size >= 2) {
        actions[1].throwExpected("More than one action property is not allowed", cliDsl)
    }

    if (actions.size == 1) {
        val child = actions.first()
        return child.assertRequire(cliDsl) { findActionValue() }.let {
            it.findStrintTemplate()?.let {
                TemplateActionValue(
                    decodeTemplate(cliDsl, it)
                )
            } ?: it.SCOPE_PARAMS()?.text?.let { ScopeParamsActionValue() }
                ?: it.assertRequire(cliDsl) { findCustomScript() }.let {
                    CustomScriptActionValue(
                        it.IDENTIFIER()?.text,
                        it.assertRequire(cliDsl) { CustomScript_SCRIPT() }.text
                    )
                }
        }
    }
    return noopAction()
}

private fun destructureFunc(cliDsl: String, func: DslParser.FuncContext) =
    Triple(
        func.assertRequire(cliDsl) { findFuncBody() }.findFuncStatements(),
        func.findParams()?.findParam().orEmpty(),
        decodeDocstring(cliDsl, func.findDocstring())
    )

private fun decodeDefaults(cliDsl: String, funcStatements: List<DslParser.FuncStatementsContext>, params: List<DslParser.ParamContext>): Map<String, String> =
    params.associate {
        it.assertRequire(cliDsl) { IDENTIFIER() }.text to it.findLiteral()?.let { decodeSimpleString(cliDsl, it) }
    }.filterNotNullValues() + funcStatements.mapNotNull { it.findDefaultOverride() }.associate {
        it.assertRequire(cliDsl) { IDENTIFIER() }.text to
            decodeSimpleString(cliDsl, it.assertRequire(cliDsl) { findLiteral() })
    }

private fun decodeConstants(cliDsl: String, statements: List<DslParser.FuncStatementsContext>): Array<Constant> =
    statements.mapNotNull { it.findConstDef() }
        .map { context ->
            context.assertRequire(cliDsl) { findLiteral() }.let { literal ->
                Constant(
                    name = context.assertRequire(cliDsl) { IDENTIFIER() }.text,
                    type = if (literal.findStrintTemplate() != null) Ref.Type.Arg else Ref.Type.Flag,
                    value = getLiteralTemplate(cliDsl, literal)
                )
            }
        }.toTypedArray()

private fun getLiteralTemplate(cliDsl: String, literal: DslParser.LiteralContext): Template =
    literal
        .findStrintTemplate()
        ?.let { decodeTemplate(cliDsl, it) }
        ?: Template(listOf(SimpleString(literal.assertRequire(cliDsl) { findBooleanLiteral() }.text)))

private fun decodeSimpleString(cliDsl: String, literal: DslParser.LiteralContext): String {
    val tpl = getLiteralTemplate(cliDsl, literal)
    if (tpl.refReferences.any()) {
        literal.throwExpected("String interpolation not supported for defaults", cliDsl)
        // TODO maybe we should support it?
    }
    return tpl.str(emptyList())
}

private fun decodeChildren(cliDsl: String, statements: List<DslParser.FuncStatementsContext>): Array<DescendantToolchain> {
    val children = statements.mapNotNull { it.findChildren() }
        .toList()
    if (children.size >= 2) {
        children[1].throwExpected("More than one child property not allowed", cliDsl)
    }
    if (children.size == 1) {
        val child = children.first()
        return child.findFunc().map { decodeDescendantToolchain(cliDsl, it) }.toTypedArray()
    }
    return emptyArray()
}

private fun decodeParameters(cliDsl: String, params: List<DslParser.ParamContext>, paramDescriptions: Map<String, String>): Array<ParamDefinition> =
    params.map { parsedParam ->
        val paramName = parsedParam.assertRequire(cliDsl) { IDENTIFIER() }.text

        ParamDefinition(
            name = parsedParam.assertRequire(cliDsl) { IDENTIFIER() }.text,
            description = paramDescriptions[paramName] ?: emptyString(),
            optional = parsedParam.QMARK() != null,
            shorthand = parsedParam.ALPHANUMERIC()?.text,
            type = parsedParam.assertRequire(cliDsl) { findParamType() }.let {
                when {
                    it.FLAG() != null -> Ref.Type.Flag
                    it.ARGUMENT() != null -> Ref.Type.Arg
                    else -> it.throwUnexpected("Could not parse parameter type", cliDsl)
                }
            }
        )
    }.toTypedArray()

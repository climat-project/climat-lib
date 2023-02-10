package parser

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
import parser.template.SimpleString
import parser.template.Template
import parser.template.decodeTemplate

internal fun decodeCliDsl(cliDsl: String): RootToolchain {
    val lexer = DslLexer(CharStreams.fromString(cliDsl))
    val parser = DslParser(CommonTokenStream(lexer))
    parser.addErrorListener(errListener)

    val func = parser.root().assertRequire { findFunc() }

    val (statements, params, docstring) = destructureFunc(func)

    return RootToolchain(
        name = func.assertRequire { IDENTIFIER() }.text,
        description = docstring.functionDoc,
        parameters = decodeParameters(params, docstring.paramDoc),
        parameterDefaults = decodeDefaults(statements, params),
        action = decodeAction(statements),
        children = decodeChildren(statements),
        constants = decodeConstants(statements),
        resources = emptyArray()
    )
}

private fun decodeDescendantToolchain(func: DslParser.FuncContext): DescendantToolchain {
    val (statements, params, docstring) = destructureFunc(func)

    return DescendantToolchain(
        name = func.assertRequire { IDENTIFIER() }.text,
        description = docstring.functionDoc,
        parameters = decodeParameters(params, docstring.paramDoc),
        parameterDefaults = decodeDefaults(statements, params),
        action = decodeAction(statements),
        children = decodeChildren(statements),
        constants = decodeConstants(statements),
        aliases = decodeAliases(statements)
    )
}

private fun decodeAliases(statements: List<DslParser.FuncStatementsContext>): Array<String> {
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

private fun decodeAction(statements: List<DslParser.FuncStatementsContext>): ActionValueBase<*> {
    val actions = statements.mapNotNull { it.findAction() }
        .toList()
    if (actions.size >= 2) {
        actions[1].throwExpected("More than one action property is not allowed")
    }

    if (actions.size == 1) {
        val child = actions.first()
        return child.assertRequire { findActionValue() }.let {
            it.findStrintTemplate()?.let {
                TemplateActionValue(
                    decodeTemplate(it)
                )
            } ?: it.SCOPE_PARAMS()?.text?.let { ScopeParamsActionValue() }
                ?: it.assertRequire { findCustomScript() }.let {
                    CustomScriptActionValue(
                        it.IDENTIFIER()?.text,
                        it.assertRequire { CustomScript_SCRIPT() }.text
                    )
                }
        }
    }
    return noopAction()
}

private fun destructureFunc(func: DslParser.FuncContext) =
    Triple(
        func.assertRequire { findFuncBody() }.findFuncStatements(),
        func.findParams()?.findParam().orEmpty(),
        decodeDocstring(func.findDocstring())
    )

private fun decodeDefaults(funcStatements: List<DslParser.FuncStatementsContext>, params: List<DslParser.ParamContext>): Map<String, String> =
    params.associate {
        it.assertRequire { IDENTIFIER() }.text to it.findLiteral()?.let(::decodeSimpleString)
    }.filterNotNullValues() + funcStatements.mapNotNull { it.findDefaultOverride() }.associate {
        it.assertRequire { IDENTIFIER() }.text to
            decodeSimpleString(it.assertRequire { findLiteral() })
    }

private fun decodeConstants(statements: List<DslParser.FuncStatementsContext>): Array<Constant> =
    statements.mapNotNull { it.findConstDef() }
        .map { context ->
            context.assertRequire { findLiteral() }.let { literal ->
                Constant(
                    name = context.assertRequire { IDENTIFIER() }.text,
                    type = if (literal.findStrintTemplate() != null) Ref.Type.Arg else Ref.Type.Flag,
                    value = getLiteralTemplate(literal)
                )
            }
        }.toTypedArray()

private fun getLiteralTemplate(literal: DslParser.LiteralContext): Template =
    literal
        .findStrintTemplate()
        ?.let(::decodeTemplate)
        ?: Template(listOf(SimpleString(literal.assertRequire { findBooleanLiteral() }.text)))

private fun decodeSimpleString(literal: DslParser.LiteralContext): String {
    val tpl = getLiteralTemplate(literal)
    if (tpl.refReferences.any()) {
        literal.throwExpected("String interpolation not supported for defaults")
        // TODO maybe we should support it?
    }
    return tpl.str(emptyList())
}

private fun decodeChildren(statements: List<DslParser.FuncStatementsContext>): Array<DescendantToolchain> {
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

private fun decodeParameters(params: List<DslParser.ParamContext>, paramDescriptions: Map<String, String>): Array<ParamDefinition> =
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

package com.climat.library.parser.dsl

import climat.lang.DslLexer
import climat.lang.DslParser
import com.climat.library.domain.action.ActionValueBase
import com.climat.library.domain.action.CustomScriptActionValue
import com.climat.library.domain.action.ScopeParamsActionValue
import com.climat.library.domain.action.TemplateActionValue
import com.climat.library.domain.ref.Constant
import com.climat.library.domain.ref.ParamDefinition
import com.climat.library.domain.ref.Ref
import com.climat.library.domain.toolchain.DescendantToolchain
import com.climat.library.domain.toolchain.RootToolchain
import com.climat.library.parser.docstring.decodeDocstring
import com.climat.library.parser.exception.CliDslErrorListener
import com.climat.library.parser.exception.assertRequire
import com.climat.library.parser.exception.throwExpected
import com.climat.library.parser.exception.throwUnexpected
import com.climat.library.parser.template.SimpleString
import com.climat.library.parser.template.Template
import com.climat.library.parser.template.decodeTemplate
import com.climat.library.utils.emptyString
import com.climat.library.utils.filterNotNullValues
import com.climat.library.utils.noopAction
import com.climat.library.utils.then
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream

internal fun decodeCliDsl(cliDsl: String): RootToolchain {
    val lexer = DslLexer(CharStreams.fromString(cliDsl))
    val parser = DslParser(CommonTokenStream(lexer))
    parser.addErrorListener(CliDslErrorListener(cliDsl))

    val root = parser.root()

    val (statements, params, docstring) = destructure(cliDsl, root)

    return RootToolchain(
        name = root.assertRequire(cliDsl) { IDENTIFIER() }.text,
        description = docstring.subDoc,
        parameters = decodeParameters(cliDsl, params, docstring.paramDoc),
        parameterDefaults = decodeRootDefaults(cliDsl, params),
        action = decodeRootAction(cliDsl, statements),
        children = decodeRootChildren(cliDsl, statements),
        constants = decodeRootConstants(cliDsl, statements),
        resources = emptyArray()
    )
}

private fun decodeSub(cliDsl: String, sub: DslParser.SubContext): DescendantToolchain {
    val (statements, params, docstring, modifiers) = destructure(cliDsl, sub)

    return DescendantToolchain(
        name = sub.assertRequire(cliDsl) { IDENTIFIER() }.text,
        description = docstring.subDoc,
        parameters = decodeParameters(cliDsl, params, docstring.paramDoc),
        parameterDefaults = decodeSubDefaults(cliDsl, statements, params),
        action = decodeSubAction(cliDsl, statements),
        children = decodeSubChildren(cliDsl, statements),
        constants = decodeSubConstants(cliDsl, statements),
        aliases = decodeSubAliases(cliDsl, modifiers)
    )
}

private fun decodeSubAliases(cliDsl: String, statements: List<DslParser.SubModifiersContext>): Array<String> {
    val aliases =
        statements.mapNotNull { it.findAliasesModifier() }.flatMap { it.IDENTIFIER() }.map { it.text } +
            statements.mapNotNull { it.findAliasModifier() }.map { it.assertRequire(cliDsl) { IDENTIFIER() }.text }
    return aliases.toTypedArray()
}

private fun decodeSubAction(cliDsl: String, statements: List<DslParser.SubStatementsContext>): ActionValueBase<*> =
    decodeRootAction(cliDsl, statements.mapNotNull { it.findRootStatements() })
private fun decodeRootAction(cliDsl: String, statements: List<DslParser.RootStatementsContext>): ActionValueBase<*> {
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

private fun destructure(cliDsl: String, sub: DslParser.SubContext) =
    sub.assertRequire(cliDsl) { findSubBody() }.findSubStatements() then
        sub.findParams()?.findParam().orEmpty() then
        decodeDocstring(cliDsl, sub.findDocstring()) then
        sub.findSubModifiers()

private fun destructure(cliDsl: String, root: DslParser.RootContext) =
    root.assertRequire(cliDsl) { findRootBody() }.findRootStatements() then
        root.findParams()?.findParam().orEmpty() then
        decodeDocstring(cliDsl, root.findDocstring())

private fun decodeSubDefaults(cliDsl: String, subStatements: List<DslParser.SubStatementsContext>, params: List<DslParser.ParamContext>): Map<String, String> =
    decodeRootDefaults(cliDsl, params) + subStatements.mapNotNull { it.findDefaultOverride() }.associate {
        it.assertRequire(cliDsl) { IDENTIFIER() }.text to
            decodeSimpleString(cliDsl, it.assertRequire(cliDsl) { findLiteral() })
    }

private fun decodeRootDefaults(cliDsl: String, params: List<DslParser.ParamContext>): Map<String, String> =
    params.associate {
        it.assertRequire(cliDsl) { IDENTIFIER() }.text to it.findLiteral()?.let { decodeSimpleString(cliDsl, it) }
    }.filterNotNullValues()

private fun decodeSubConstants(cliDsl: String, statements: List<DslParser.SubStatementsContext>): Array<Constant> =
    decodeRootConstants(cliDsl, statements.mapNotNull { it.findRootStatements() })
private fun decodeRootConstants(cliDsl: String, statements: List<DslParser.RootStatementsContext>): Array<Constant> =
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

private fun decodeSubChildren(cliDsl: String, statements: List<DslParser.SubStatementsContext>): Array<DescendantToolchain> =
    decodeRootChildren(cliDsl, statements.mapNotNull { it.findRootStatements() })
private fun decodeRootChildren(cliDsl: String, statements: List<DslParser.RootStatementsContext>): Array<DescendantToolchain> {
    val children = statements.mapNotNull { it.findChildren() }
        .toList()
    if (children.size >= 2) {
        children[1].throwExpected("More than one child property not allowed", cliDsl)
    }
    if (children.size == 1) {
        val child = children.first()
        return child.findSub().map { decodeSub(cliDsl, it) }.toTypedArray()
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

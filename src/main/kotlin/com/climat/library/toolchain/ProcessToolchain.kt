package com.climat.library.toolchain

import com.climat.library.domain.action.ActionValueBase
import com.climat.library.domain.action.CustomScriptActionValue
import com.climat.library.domain.action.NoopActionValue
import com.climat.library.domain.action.ScopeParamsActionValue
import com.climat.library.domain.action.TemplateActionValue
import com.climat.library.domain.isLeaf
import com.climat.library.domain.ref.RefWithValue
import com.climat.library.domain.toolchain.DescendantToolchain
import com.climat.library.domain.toolchain.RootToolchain
import com.climat.library.domain.toolchain.Toolchain
import com.climat.library.toolchain.exception.ParameterMissingException
import com.climat.library.toolchain.exception.ToolchainNotDefinedException
import com.climat.library.utils.newLines

internal fun processToolchain(
    toolchain: RootToolchain,
    params: MutableList<String>,
    handler: (parsedAction: ActionValueBase<*>, context: Toolchain) -> Unit
) {
    val next = params.removeFirst()
    if (toolchain.name != next) throw ToolchainNotDefinedException(next)

    val scopeRefs = processRefs(toolchain, params, emptyList())

    if (params.isEmpty()) {
        val act = toolchain.action
        if (act.type != ActionValueBase.Type.Noop) {
            setActualCommand(act, scopeRefs.values)
            handler(act, toolchain)
        }
    } else if (toolchain.isLeaf) {
        throw Exception() // TODO: proper error
    } else {
        processToolchain(
            children = toolchain.children,
            params = params,
            upperScopeRefs = scopeRefs,
            handler = handler,
            pathToRoot = listOf(toolchain)
        )
    }
}

private fun processToolchain(
    children: Array<DescendantToolchain>,
    params: MutableList<String>,
    upperScopeRefs: Map<String, RefWithValue>,
    handler: (parsedAction: ActionValueBase<*>, context: Toolchain) -> Unit,
    pathToRoot: List<Toolchain>
) {
    val next = params.removeFirst()
    val matchingToolchain = children.find {
        it.name != "_" && (it.name == next || it.aliases.any { it.name == next })
    } ?: children.find { it.name == "_" }
        ?: throw Exception("No sub matching with $next") // TODO: proper error

    val scopeRefs = upperScopeRefs + processRefs(matchingToolchain, params, emptyList())

    if (params.isEmpty()) {
        val act = matchingToolchain.action
        if (act.type != ActionValueBase.Type.Noop) {
            setActualCommand(act, scopeRefs.values)
            handler(act, matchingToolchain)
        }
    } else if (matchingToolchain.isLeaf) {
        throw Exception() // TODO: proper error
    } else {
        processToolchain(
            children = matchingToolchain.children,
            params = params,
            upperScopeRefs = scopeRefs,
            handler = handler,
            pathToRoot = pathToRoot + matchingToolchain
        )
    }
}

private fun processRefs(
    toolchain: Toolchain,
    params: MutableList<String>,
    pathToRoot: List<Toolchain>
) = try {
    processRefs(toolchain, params)
} catch (ex: ParameterMissingException) {
    throw Exception("Parameter ${ex.arg.name} is missing${newLines(1)}${toolchain.getUsageHint(pathToRoot)}", ex)
}

private fun setActualCommand(
    action: ActionValueBase<*>,
    values: Collection<RefWithValue>
) {
    when (action) {
        is TemplateActionValue -> {
            action.value = action.template.str(values)
        }

        is CustomScriptActionValue -> {
            action.value = values.associate { it.ref.name to it.value }
        }

        is ScopeParamsActionValue -> {
            action.value = values.associate { it.ref.name to it.value }
        }

        is NoopActionValue -> {
            // By definition, do nothing
        }

        else -> throw Exception("Type `${action::class}` not supported")
    }
}

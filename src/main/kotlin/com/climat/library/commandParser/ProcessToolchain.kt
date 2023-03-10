package com.climat.library.commandParser

import com.climat.library.commandParser.exception.ParameterException
import com.climat.library.domain.action.ActionValueBase
import com.climat.library.domain.action.CustomScriptActionValue
import com.climat.library.domain.action.NoopActionValue
import com.climat.library.domain.action.ScopeParamsActionValue
import com.climat.library.domain.action.TemplateActionValue
import com.climat.library.domain.isLeaf
import com.climat.library.domain.ref.RefWithAnyValue
import com.climat.library.domain.toolchain.RootToolchain
import com.climat.library.domain.toolchain.Toolchain
import com.climat.library.utils.newLine

internal fun processRootToolchain(
    toolchain: RootToolchain,
    params: MutableList<String>,
    handler: (parsedAction: ActionValueBase<*>, context: Toolchain) -> Unit
) = processToolchainDescendants(
    children = listOf(toolchain),
    params = params,
    upperScopeRefs = emptyMap(),
    upperPathToRoot = emptyList(),
    handler = handler
)

private fun processToolchainDescendants(
    children: List<Toolchain>,
    params: MutableList<String>,
    upperScopeRefs: Map<String, RefWithAnyValue>,
    upperPathToRoot: List<Toolchain>,
    handler: (parsedAction: ActionValueBase<*>, context: Toolchain) -> Unit,
) {
    require(params.any()) {
        "`params` must have at least one element"
    }

    val next = params.removeFirst()
    val toolchain = children.find {
        it.name != "_" && (it.name == next || it.aliases.any { it.name == next })
    } ?: children.find { it.name == "_" }
        ?: throw Exception("Toolchain $next is not defined" + newLine() + getSubcommandUsageHint(upperPathToRoot))
    processToolchain(
        params = params,
        toolchain = toolchain,
        upperScopeRefs = upperScopeRefs,
        upperPathToRoot = upperPathToRoot,
        handler = handler,
    )
}

private fun processToolchain(
    params: MutableList<String>,
    toolchain: Toolchain,
    upperScopeRefs: Map<String, RefWithAnyValue>,
    upperPathToRoot: List<Toolchain>,
    handler: (parsedAction: ActionValueBase<*>, context: Toolchain) -> Unit
) {
    val pathToRoot = upperPathToRoot + toolchain
    val scopeRefs = upperScopeRefs + processRefs(toolchain, params, pathToRoot)
    if (params.isEmpty()) {
        val act = toolchain.action
        if (act.type != ActionValueBase.Type.Noop) {
            setActualCommand(act, scopeRefs.values)
            handler(act, toolchain)
        }
    } else if (toolchain.isLeaf) {
        throw Exception() // TODO: proper error
    } else {
        processToolchainDescendants(
            children = toolchain.children.toList(),
            params = params,
            upperScopeRefs = scopeRefs,
            upperPathToRoot = pathToRoot,
            handler = handler
        )
    }
}

private fun processRefs(
    toolchain: Toolchain,
    params: MutableList<String>,
    pathToRoot: List<Toolchain>
): Map<String, RefWithAnyValue> = try {
    processRefs(toolchain, params)
} catch (ex: ParameterException) {
    throw Exception(
        ex.message + newLine() + getParameterUsageHint(pathToRoot),
        ex
    )
}

private fun setActualCommand(
    action: ActionValueBase<*>,
    values: Collection<RefWithAnyValue>
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

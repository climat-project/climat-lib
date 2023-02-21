package com.climat.library.toolchain

import com.climat.library.domain.action.ActionValueBase
import com.climat.library.domain.action.CustomScriptActionValue
import com.climat.library.domain.action.NoopActionValue
import com.climat.library.domain.action.ScopeParamsActionValue
import com.climat.library.domain.action.TemplateActionValue
import com.climat.library.domain.isLeaf
import com.climat.library.domain.ref.Ref
import com.climat.library.domain.ref.RefWithValue
import com.climat.library.domain.toolchain.DescendantToolchain
import com.climat.library.domain.toolchain.RootToolchain
import com.climat.library.domain.toolchain.Toolchain

val ARG_PREFIX = "--"
val SHORTHAND_ARG_PREFIX = "-"
internal fun processToolchain(
    toolchain: RootToolchain,
    params: MutableList<String>,
    handler: (parsedAction: ActionValueBase<*>, context: Toolchain) -> Unit
) {
    val c = params.removeFirst()
    if (toolchain.name != c) throw Exception("Cannot find toolchain with name $c") // TODO: proper error
    val scopeRefs = processRefs(toolchain, params)

    if (params.isEmpty()) {
        val act = toolchain.action
        if (act.type != ActionValueBase.Type.Noop) {
            setActualCommand(act, scopeRefs.values)
            handler(act, toolchain)
        }
    } else if (toolchain.isLeaf) {
        throw Exception() // TODO: proper error
    } else {
        processToolchain(toolchain.children, params, scopeRefs, handler)
    }
}
private fun processToolchain(
    children: Array<DescendantToolchain>,
    params: MutableList<String>,
    upperScopeRefs: Map<String, RefWithValue>,
    handler: (parsedAction: ActionValueBase<*>, context: Toolchain) -> Unit
) {
    val c = params.removeFirst()
    val matching = children.filter {
        it.name != "_" && (it.name == c || c in it.aliases)
    }

    val onlyMatching =
        when (matching.size) {
            0 -> children.find {
                it.name == "_"
            } ?: throw Exception("No sub matching with $c") // TODO: proper error

            1 -> matching.single()

            else -> // Ambiguous, cannot proceed. Should not happen
                throw Exception() // TODO: proper error
        }

    val scopeRefs = upperScopeRefs + processRefs(onlyMatching, params)

    if (params.isEmpty()) {
        val act = onlyMatching.action
        if (act.type != ActionValueBase.Type.Noop) {
            setActualCommand(act, scopeRefs.values)
            handler(act, onlyMatching)
        }
    } else if (onlyMatching.isLeaf) {
        throw Exception() // TODO: proper error
    } else {
        processToolchain(onlyMatching.children, params, scopeRefs, handler)
    }
}

internal fun processRefs(
    toolchain: Toolchain,
    params: MutableList<String>
): Map<String, RefWithValue> {
    val (optional, required) = toolchain.parameters.partition { it.optional }
    val ans = required.associate {
        it.name to RefWithValue(it, params.removeFirst())
    }.toMutableMap()

    val optionals = optional.associateBy { it.name }.toMutableMap()
    while (optionals.isNotEmpty() && params.isNotEmpty() && params.first().startsWith(ARG_PREFIX)) {
        val next = params.removeFirst().drop(ARG_PREFIX.length)

        val param = optionals.remove(next) ?: throw Exception("Argument $next not defined") // TODO: proper error

        ans[next] = RefWithValue(
            param,
            if (param.type == Ref.Type.Flag) {
                true.toString()
            } else {
                params.removeFirst()
            }
        )
    }

    optionals.values.filter { it.type != Ref.Type.Flag }
        .forEach {
            val default = toolchain.parameterDefaults[it.name] ?: throw Exception("No value provided for ${it.name}")
            ans[it.name] = RefWithValue(it, default)
        }
    optionals.values.filter { it.type == Ref.Type.Flag }.forEach { ans[it.name] = RefWithValue(it, false.toString()) }

    return ans + toolchain.constants.fold(emptyList<RefWithValue>()) { acc, it ->
        acc + listOf(RefWithValue(it, it.value.str(acc)))
    }.associateBy { it.ref.name }
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

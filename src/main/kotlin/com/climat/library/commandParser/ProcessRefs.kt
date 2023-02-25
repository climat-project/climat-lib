package com.climat.library.commandParser

import com.climat.library.commandParser.exception.ParameterMissingException
import com.climat.library.commandParser.exception.ParameterNotDefinedException
import com.climat.library.domain.ref.ArgDefinition
import com.climat.library.domain.ref.FlagDefinition
import com.climat.library.domain.ref.ParamDefinition
import com.climat.library.domain.ref.RefWithValue
import com.climat.library.domain.toolchain.Toolchain
import com.climat.library.utils.emptyString

// TODO: make those configurable
const val ARG_PREFIX = "--"
const val SHORTHAND_ARG_PREFIX = "-"
internal fun processRefs(
    toolchain: Toolchain,
    params: MutableList<String>
): Map<String, RefWithValue> {
    val (optionals, required) = toolchain.parameters.partition { it.optional }
    val nameToOptionals = optionals.associateBy { it.name }
    val shortHandToOptionals = optionals.associateBy { it.shorthand }
    val optionalsSet = optionals.toMutableSet()

    val ans = required.associate {
        it.name to RefWithValue(it, params.removeFirst(it))
    }.toMutableMap()

    while (optionalsSet.isNotEmpty() && params.isNotEmpty()) {
        val first = params.first()
        val newParams = when {
            first.startsWith(ARG_PREFIX) -> getParamsFromNamePrefixed(params, nameToOptionals)
            first.startsWith(SHORTHAND_ARG_PREFIX) -> getParamsFromShorthandPrefixed(params, shortHandToOptionals)
            else -> break
        }
        ans += newParams
        optionalsSet.removeAll(newParams.values.map { it.ref }.toSet())
    }

    ans +=
        optionalsSet
            .associate {
                it.name to RefWithValue(
                    it,
                    when (it) {
                        is ArgDefinition -> it.default ?: emptyString()
                        is FlagDefinition -> false.toString()
                        else -> throw Exception("Type `${it::class.simpleName}` is not supported")
                    }
                )
            }

    return ans + toolchain.constants.fold(emptyList<RefWithValue>()) { acc, it ->
        acc + listOf(RefWithValue(it, it.value.str(acc)))
    }.associateBy { it.ref.name }
}

private fun getParamsFromShorthandPrefixed(
    params: MutableList<String>,
    shortHandToOptionals: Map<String?, ParamDefinition>
): Map<String, RefWithValue> {
    val next = params.removeFirst().drop(SHORTHAND_ARG_PREFIX.length)

    return when {
        next.length > 1 -> getFlagsFromManyShorthands(shortHandToOptionals, next)
        next.length == 1 -> getParamsFromSingleShorthand(shortHandToOptionals, next, params)
        else -> throw Exception("Cannot pass empty shorthand")
    }
}

private fun getParamsFromSingleShorthand(
    shortHandToOptionals: Map<String?, ParamDefinition>,
    next: String,
    params: MutableList<String>
): Map<String, RefWithValue> {
    val ref = shortHandToOptionals[next] ?: throw ParameterNotDefinedException(next)
    return mapOf(
        ref.name to RefWithValue(
            ref,
            when (ref) {
                is FlagDefinition -> true.toString()
                is ArgDefinition -> params.removeFirst()
                else -> throw Exception("Type is not supported") // TODO proper error
            }
        )
    )
}

private fun getFlagsFromManyShorthands(
    shortHandToOptionals: Map<String?, ParamDefinition>,
    next: String
) = next.map {
    shortHandToOptionals[it.toString()] ?: throw ParameterNotDefinedException(next)
}.onEach {
    if (it is FlagDefinition) {
        throw Exception("Parameter ${it.name} cannot be used as a flag")
    }
}
    .associate {
        it.name to RefWithValue(
            it,
            true.toString()
        )
    }

private fun getParamsFromNamePrefixed(
    params: MutableList<String>,
    nameToOptionals: Map<String, ParamDefinition>
): Map<String, RefWithValue> {
    val next = params.removeFirst().drop(ARG_PREFIX.length)
    if (next.isEmpty()) {
        throw Exception("Cannot pass empty arg name") // TODO proper error
    }
    val ref = nameToOptionals[next] ?: throw ParameterNotDefinedException(next)
    return mapOf(
        next to RefWithValue(
            ref,
            when (ref) {
                is ArgDefinition -> params.removeFirst()
                is FlagDefinition -> true.toString()
                else -> throw Exception("Type is not supported")
            }
        )
    )
}

private fun MutableList<String>.removeFirst(param: ParamDefinition): String =
    removeFirstOrNull() ?: throw ParameterMissingException(param)

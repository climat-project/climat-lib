package com.climat.library.commandParser

import com.climat.library.commandParser.exception.ParameterException
import com.climat.library.commandParser.exception.ParameterNotDefinedException
import com.climat.library.domain.ref.ArgDefinition
import com.climat.library.domain.ref.FlagDefinition
import com.climat.library.domain.ref.ParamDefinition
import com.climat.library.domain.ref.RefWithAnyValue
import com.climat.library.domain.ref.RefWithValue
import com.climat.library.domain.toolchain.Toolchain
import com.climat.library.utils.emptyString

// TODO: make those configurable
const val ARG_PREFIX = "--"
const val SHORTHAND_ARG_PREFIX = "-"
internal fun processRefs(
    toolchain: Toolchain,
    params: MutableList<String>
): Map<String, RefWithAnyValue> {
    val (optionals, required) = toolchain.parameters.partition { it.optional }
    val nameToOptionals = optionals.associateBy { it.name }
    val shortHandToOptionals = optionals.associateBy { it.shorthand }
    val optionalsSet = optionals.toMutableSet()

    val ans = required.associate { paramDef ->
        paramDef.name to RefWithValue(paramDef, getNextValue(paramDef, params)) as RefWithAnyValue
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

    processPredefined(toolchain, ans, params)

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

    return ans + toolchain.constants.fold(emptyList<RefWithValue<String>>()) { acc, it ->
        acc + listOf(RefWithValue(it, it.value.str(acc)))
    }.associateBy { it.ref.name }
}

fun getNextValue(paramDef: ParamDefinition, params: MutableList<String>): String {
    val paramValue =
        params.removeFirstOrNull() ?: throw ParameterException(
            paramDef,
            "Parameter ${paramDef.name} needs a value"
        )
    if (paramValue.startsWith(ARG_PREFIX) || paramValue.startsWith(SHORTHAND_ARG_PREFIX)) {
        throw ParameterException(
            paramDef,
            "Parameter ${paramDef.name} is not a flag, and needs an appropriate value."
        )
    }
    return paramValue
}

private fun processPredefined(
    toolchain: Toolchain,
    ans: MutableMap<String, RefWithAnyValue>,
    params: MutableList<String>
) {
    // Process unmatched
    val unmatchedPredefined = toolchain.predefinedParameters.find { it.name == "__UNMATCHED" } ?: return
    ans[unmatchedPredefined.name] = RefWithValue(unmatchedPredefined, params.toTypedArray())
    params.clear()
}

private fun getParamsFromShorthandPrefixed(
    params: MutableList<String>,
    shortHandToOptionals: Map<String?, ParamDefinition>
): Map<String, RefWithValue<String>> {
    val next = params.removeFirst().drop(SHORTHAND_ARG_PREFIX.length)

    return when {
        next.length > 1 -> getFlagsFromManyShorthands(shortHandToOptionals, next)
        next.length == 1 -> getParamsFromSingleShorthand(shortHandToOptionals, next, params)
        else -> throw Exception("Cannot pass empty shorthand")
    }
}

private fun getParamsFromSingleShorthand(
    shortHandToOptionals: Map<String?, ParamDefinition>,
    name: String,
    params: MutableList<String>
): Map<String, RefWithValue<String>> {
    val paramDef = shortHandToOptionals[name] ?: throw ParameterNotDefinedException(name)
    return mapOf(
        paramDef.name to RefWithValue(
            paramDef,
            when (paramDef) {
                is FlagDefinition -> true.toString()
                is ArgDefinition -> getNextValue(paramDef, params)
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
): Map<String, RefWithValue<String>> {
    val name = params.removeFirst().drop(ARG_PREFIX.length)
    if (name.isEmpty()) {
        throw Exception("Cannot pass empty arg name") // TODO proper error
    }
    val paramDef = nameToOptionals[name] ?: throw ParameterNotDefinedException(name)
    return mapOf(
        name to RefWithValue(
            paramDef,
            when (paramDef) {
                is ArgDefinition -> getNextValue(paramDef, params)
                is FlagDefinition -> true.toString()
                else -> throw Exception("Type is not supported")
            }
        )
    )
}

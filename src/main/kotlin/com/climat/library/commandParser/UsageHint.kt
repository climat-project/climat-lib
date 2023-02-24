package com.climat.library.commandParser

import com.climat.library.domain.ref.ParamDefinition
import com.climat.library.domain.toolchain.Toolchain
import com.climat.library.utils.newLine
import com.climat.library.utils.newLines
import com.climat.library.utils.tpl

internal fun getUsageHint(parameters: Array<ParamDefinition>, pathToRoot: List<Toolchain>): String {
    val (optionals, required) = parameters.partition { it.optional }
    return getRequiredPlaceholders(required, pathToRoot) +
        newLines(2) +
        getOptionalsUsage(optionals) +
        newLine()
}

internal fun getUsageHint(pathToRoot: List<Toolchain>): String {
    return "Available subcommands:${newLines(2)}" +
        pathToRoot.last().children.joinToString(newLine()) {
            "${it.name} - ${it.description}"
        } +
        newLine()
}

private fun getRequiredPlaceholders(required: List<ParamDefinition>, pathToRoot: List<Toolchain>) =
    "usage: " +
        pathToRoot.joinToString(" ") { it.name } +
        " " +
        required.joinToString(" ") { "<${it.name}>" }

fun getOptionalsUsage(optionals: List<ParamDefinition>) =
    optionals.joinToString(newLine()) {
        ARG_PREFIX +
            it.name +
            "," +
            it.shorthand.tpl { "${SHORTHAND_ARG_PREFIX}$it" } +
            " \t\t ${it.description} "
    }

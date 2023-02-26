@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package com.climat.library.commandParser

import com.climat.library.domain.action.Action
import com.climat.library.domain.toolchain.RootToolchain
import com.climat.library.domain.toolchain.Toolchain
import com.climat.library.dslParser.dsl.decodeCliDsl
import com.climat.library.validation.computeValidations
import com.climat.library.validation.validate
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

fun getValidations(toolchain: RootToolchain) = computeValidations(toolchain).toList().toTypedArray()

fun parse(cliDsl: String): RootToolchain =
    decodeCliDsl(cliDsl)

fun execute(
    args: Array<String>,
    toolchain: RootToolchain,
    actionHandler: (parsedAction: Action, context: Toolchain) -> Unit,
    skipValidation: Boolean = false
) {
    if (!skipValidation)
        validate(toolchain)
    val mutableArgs = args.toMutableList()
    mutableArgs.add(0, toolchain.name)
    processRootToolchain(toolchain, mutableArgs, actionHandler)
}

@JsName("executeFromCliDsl")
fun execute(
    args: Array<String>,
    cliDsl: String,
    actionHandler: (parsedAction: Action, context: Toolchain) -> Unit,
    skipValidation: Boolean = false
) {
    execute(args, parse(cliDsl), actionHandler, skipValidation)
}

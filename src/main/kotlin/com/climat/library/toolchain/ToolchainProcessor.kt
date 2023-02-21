package com.climat.library.toolchain

import com.climat.library.domain.action.Action
import com.climat.library.domain.toolchain.RootToolchain
import com.climat.library.domain.toolchain.Toolchain
import com.climat.library.parser.dsl.decodeCliDsl
import com.climat.library.validation.computeValidations
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import com.climat.library.validation.validate as _validate

@OptIn(ExperimentalJsExport::class)
@JsExport
class ToolchainProcessor {
    companion object {
        fun validate(toolchain: RootToolchain) = computeValidations(toolchain).toList().toTypedArray()
        fun parse(cliDsl: String): RootToolchain =
            decodeCliDsl(cliDsl)
    }

    @JsName("createFromCliDslString")
    constructor(cliDsl: String, actionHandler: (parsedAction: Action, context: Toolchain) -> Unit, skipValidation: Boolean = false) :
        this(decodeCliDsl(cliDsl), actionHandler, skipValidation)

    @JsName("create")
    constructor(toolchain: RootToolchain, actionHandler: (parsedAction: Action, context: Toolchain) -> Unit, skipValidation: Boolean = false) {
        if (!skipValidation)
            _validate(toolchain)
        this.toolchain = toolchain
        this.actionHandler = actionHandler
    }

    private var actionHandler: (parsedAction: Action, context: Toolchain) -> Unit
    private var toolchain: RootToolchain

    fun execute(args: Array<String>) {
        val mutableArgs = args.toMutableList()
        mutableArgs.add(0, toolchain.name)
        processToolchain(toolchain, mutableArgs, actionHandler)
    }

    @JsName("executeFromString")
    fun execute(args: String) {
        execute(
            if (args.isBlank()) {
                emptyArray()
            } else {
                args.split(Regex("\\s+")).toTypedArray()
            }
        )
    }
}

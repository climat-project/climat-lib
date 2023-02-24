package com.climat.library.toolchain.exception

import com.climat.library.domain.ref.ParamDefinition
import com.climat.library.domain.ref.Ref
import com.climat.library.domain.toolchain.Toolchain
import com.climat.library.toolchain.ARG_PREFIX
import com.climat.library.toolchain.SHORTHAND_ARG_PREFIX
import com.climat.library.utils.newLine
import com.climat.library.utils.newLines
import com.climat.library.utils.tpl

internal fun Toolchain.getUsageHint(pathToRoot: List<Toolchain>): String =
    "usage ${pathToRoot.joinToString(" ") {
        it.name
    }}${newLines(2)}${
    this.parameters.filter { it.type == Ref.Type.Flag }.joinToString(newLine()) { getParameterUsage(it) }
    }"

private fun getParameterUsage(it: ParamDefinition) =
    "${ARG_PREFIX}${it.name}," + it.shorthand.tpl { "${SHORTHAND_ARG_PREFIX}$it" } +
        " \t\t ${it.description} "

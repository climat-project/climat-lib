package com.climat.library.dslParser.dsl

import climat.lang.DslParser
import com.climat.library.domain.ref.PredefinedParamDefinition

internal fun decodeRootPredefinedParams(mods: List<DslParser.RootModifiersContext>): Array<PredefinedParamDefinition> {
    val unmatched = mods.firstNotNullOfOrNull { it.MOD_ALLOW_UNMATCHED() }
    if (unmatched != null) {
        return arrayOf(
            PredefinedParamDefinition(
                name = "__UNMATCHED",
                sourceMap = null // TODO put position
            )
        )
    }
    return emptyArray()
}

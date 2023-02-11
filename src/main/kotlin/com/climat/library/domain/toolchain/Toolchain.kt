package com.climat.library.domain.toolchain

import com.climat.library.domain.action.Action
import com.climat.library.domain.ref.Constant
import com.climat.library.domain.ref.ParamDefinition
import com.climat.library.jsExportable.JsExportableMap
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
abstract class Toolchain {
    abstract val name: String
    abstract val description: String
    abstract val parameters: Array<ParamDefinition>
    abstract val parameterDefaults: Map<String, String>
    abstract val action: Action
    abstract val children: Array<DescendantToolchain>
    abstract val constants: Array<Constant>

    val parameterDefaultsForJs: JsExportableMap<String, String>
        get() = JsExportableMap(parameterDefaults)
}

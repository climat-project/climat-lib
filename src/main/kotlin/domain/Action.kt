@file:OptIn(ExperimentalJsExport::class)

package domain

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

typealias Action = ActionValueBase<*>
@JsExport abstract class ActionValueBase<VType>() {

    var value: VType? = null

    val type: Type
        get() = when (this) {
            is TemplateActionValue -> Type.Template
            is CustomScriptActionValue -> Type.JavaScript
            is ScopeParamsActionValue -> Type.ScopeParams
            is NoopActionValue -> Type.Noop
            else -> throw Exception("${this::class} is not supported")
        }

    enum class Type {
        Template,
        JavaScript,
        ScopeParams,
        Noop
    }
}
@JsExport class TemplateActionValue(val template: String) : ActionValueBase<String>()
@JsExport class CustomScriptActionValue(val customScript: String) : ActionValueBase<Nothing>()
@JsExport class ScopeParamsActionValue() : ActionValueBase<Map<String, String>>()
@JsExport class NoopActionValue() : ActionValueBase<Nothing>()

@file:OptIn(ExperimentalJsExport::class)

package domain.action

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

typealias Action = ActionValueBase<*>
@JsExport
abstract class ActionValueBase<VType>() {

    var value: VType? = null

    val type: Type
        get() = when (this) {
            is TemplateActionValue -> Type.Template
            is CustomScriptActionValue -> Type.CustomScript
            is ScopeParamsActionValue -> Type.ScopeParams
            is NoopActionValue -> Type.Noop
            else -> throw Exception("${this::class} is not supported")
        }

    enum class Type {
        Template,
        CustomScript,
        ScopeParams,
        Noop
    }
}

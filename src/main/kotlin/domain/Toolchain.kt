package domain/*
 * domain.Toolchain.kt
 *
 * This code was generated by json-kotlin-schema-codegen - JSON Schema Code Generator
 * See https://github.com/pwall567/json-kotlin-schema-codegen
 *
 * It is not advisable to modify generated code as any modifications will be lost
 * when the generation process is re-run.
 */

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@Serializable
@JsExport
data class Toolchain(
    val name: String,
    val description: String? = null,
    private val parameters: Array<String>? = null,
    val action: String,
    val children: Array<Toolchain>? = null
) {
    @Transient
    val parsedParameters: Array<Parameter> = parameters.orEmpty().map {
        val match = ParameterRegex.find(it)
        requireNotNull(match) { "parameters item does not match pattern $ParameterRegex - $it" }
        Parameter(
            name = match.groupValues[3],
            optional = when (match.groupValues[1]) {
                "req" -> false
                "opt" -> true
                else -> throw Exception()
            },
            shorthand = match.groups[4]?.value,
            type = when (match.groupValues[2]) {
                "flag" -> Type.Flag
                "arg" -> Type.Arg
                else -> throw Exception()
            },
            description = match.groups[5]?.value
        )
    }.toTypedArray()

    enum class Type {
        Flag,
        Arg
    }

    companion object {
        private val ParameterRegex = Regex("^(req|opt):(flag|arg):(\\w+)(:\\w)?(?:: *(.*))?\$")
    }
}

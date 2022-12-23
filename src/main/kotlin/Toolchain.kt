/*
 * Toolchain.kt
 *
 * This code was generated by json-kotlin-schema-codegen - JSON Schema Code Generator
 * See https://github.com/pwall567/json-kotlin-schema-codegen
 *
 * It is not advisable to modify generated code as any modifications will be lost
 * when the generation process is re-run.
 */

import kotlinx.serialization.Serializable

@Serializable
data class Toolchain(
    val name: String,
    val description: String? = null,
    val parameters: List<Parameter>? = null,
    val action: String,
    val children: List<Toolchain>? = null
) {

    @Serializable
    data class Parameter(
        val name: String,
        val optional: Boolean = false,
        val shorthand: String? = null,
        val type: Type,
        val description: String? = null
    )

    enum class Type {
        string,
        bool,
        double,
        int
    }
}

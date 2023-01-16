package domain.dto

import emptyString
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

abstract class ToolchainDtoBase {
    abstract val name: String
    abstract val aliases: Array<String>
    abstract val description: String?
    abstract val paramDefaults: Map<String, String>
    abstract val parameters: Array<String>
    abstract val action: JsonElement?
    abstract val children: Array<ToolchainDto>
    abstract val constants: Map<String, JsonPrimitive>
}

@Serializable
data class ToolchainDto(
    override val name: String,
    override val aliases: Array<String> = emptyArray(),
    override val description: String = emptyString(),
    override val paramDefaults: Map<String, String> = emptyMap(),
    override val parameters: Array<String> = emptyArray(),
    override val action: JsonElement? = null,
    override val children: Array<ToolchainDto> = emptyArray(),
    override val constants: Map<String, JsonPrimitive> = emptyMap()
) : ToolchainDtoBase()

@Serializable
data class RootToolchainDto(
    override val name: String,
    override val aliases: Array<String> = emptyArray(),
    override val description: String = emptyString(),
    override val paramDefaults: Map<String, String> = emptyMap(),
    override val parameters: Array<String> = emptyArray(),
    override val action: JsonElement? = null,
    override val children: Array<ToolchainDto> = emptyArray(),
    override val constants: Map<String, JsonPrimitive> = emptyMap(),
    val resources: Array<String> = emptyArray()
) : ToolchainDtoBase()

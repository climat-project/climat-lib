package domain.dto

import emptyString
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@Serializable
data class RootToolchainDto(
    override val name: String,
    override val aliases: Array<String> = emptyArray(),
    override val description: String = emptyString(),
    override val paramDefaults: Map<String, String> = emptyMap(),
    override val parameters: Array<String> = emptyArray(),
    override val action: JsonElement? = null,
    override val children: Array<DescendantToolchainDto> = emptyArray(),
    override val constants: Map<String, JsonPrimitive> = emptyMap(),
    val resources: Array<String> = emptyArray()
) : ToolchainDto()

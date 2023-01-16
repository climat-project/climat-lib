package domain.dto

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

abstract class ToolchainDto {
    abstract val name: String
    abstract val aliases: Array<String>
    abstract val description: String?
    abstract val paramDefaults: Map<String, String>
    abstract val parameters: Array<String>
    abstract val action: JsonElement?
    abstract val children: Array<DescendantToolchainDto>
    abstract val constants: Map<String, JsonPrimitive>
}

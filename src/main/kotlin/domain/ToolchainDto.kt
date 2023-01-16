import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@Serializable
data class ToolchainDto(
    val name: String,
    val aliases: Array<String> = emptyArray(),
    val description: String? = null,
    val paramDefaults: Map<String, String> = emptyMap(),
    val parameters: Array<String> = emptyArray(),
    val action: JsonElement? = null,
    val children: Array<ToolchainDto> = emptyArray(),
    val constants: Map<String, JsonPrimitive> = emptyMap()
)

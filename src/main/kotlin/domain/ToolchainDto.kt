import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ToolchainDto(
    val name: String,
    val description: String? = null,
    val paramDefaults: Map<String, String>? = null,
    val parameters: Array<String>? = null,
    val action: JsonElement? = null,
    val children: Array<ToolchainDto>? = null
)

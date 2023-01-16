import domain.convert
import domain.dto.RootToolchainDto
import domain.toolchain.RootToolchain
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import validation.validate as _validate

@OptIn(ExperimentalJsExport::class)
@JsExport
class ToolchainProcessor {
    companion object {
        fun validate(toolchain: RootToolchain) = _validate(toolchain)
        fun parse(json: String): RootToolchain {
            val toolchain = Json.decodeFromString<RootToolchain>(json)
            validate(toolchain)
            return toolchain
        }
    }

    @JsName("createFromJsonString")
    constructor(json: String, actionHandler: (String) -> Unit, skipValidation: Boolean = false) :
        this(convert(Json.decodeFromString<RootToolchainDto>(json)), actionHandler, skipValidation)

    @JsName("create")
    constructor(toolchain: RootToolchain, actionHandler: (String) -> Unit, skipValidation: Boolean = false) {
        if (!skipValidation)
            validate(toolchain)
        this.parser = ToolchainCommand(toolchain, actionHandler)
    }

    private val parser: ToolchainCommand

    fun execute(args: Array<String>) {
        parser.parse(args)
    }

    @JsName("executeFromString")
    fun execute(args: String) {
        execute(args.split(Regex("\\s+")).toTypedArray())
    }
}

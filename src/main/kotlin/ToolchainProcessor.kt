import domain.Toolchain
import domain.convert
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
        fun validate(toolchain: Toolchain) = _validate(toolchain)
        fun parse(json: String): Toolchain {
            val toolchain = Json.decodeFromString<Toolchain>(json)
            validate(toolchain)
            return toolchain
        }
    }

    @JsName("createFromJsonString")
    constructor(json: String, actionHandler: (String) -> Unit, skipValidation: Boolean = false) :
        this(convert(Json.decodeFromString<ToolchainDto>(json)), actionHandler, skipValidation)

    @JsName("create")
    constructor(toolchain: Toolchain, actionHandler: (String) -> Unit, skipValidation: Boolean = false) {
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

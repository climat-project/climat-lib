import domain.Toolchain
import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import validation.validate as _validate

@OptIn(ExperimentalCli::class, ExperimentalJsExport::class)
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
        this(Json.decodeFromString<Toolchain>(json), actionHandler, skipValidation)

    @JsName("create")
    constructor(toolchain: Toolchain, actionHandler: (String) -> Unit, skipValidation: Boolean = false) {
        this.parser = ArgParser(toolchain.name, autoTerminate = false)
        if(!skipValidation)
            validate(toolchain)
        val subcommands = toolchain.children.orEmpty().map {
            ToolchainSubcommand(it, actionHandler)
        }.toTypedArray()
        parser.subcommands(*subcommands)
    }

    private val parser: ArgParser

    fun execute(args: Array<String>) {
        parser.parse(args)
    }

    @JsName("executeFromString")
    fun execute(args: String) {
        execute(args.split(Regex("\\s+")).toTypedArray())
    }
}

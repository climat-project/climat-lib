import domain.Toolchain
import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import validation.validate

@OptIn(ExperimentalCli::class, ExperimentalJsExport::class)
@JsExport
class ToolchainProcessor(json: String, actionHandler: (String) -> Unit) {

    private var toolchain = Json.decodeFromString<Toolchain>(json)
    private var parser = ArgParser(toolchain.name, autoTerminate = false)

    init {
        validate(toolchain)
        val subcommands = toolchain.children.orEmpty().map {
            ToolchainSubcommand(it, actionHandler)
        }.toTypedArray()
        parser.subcommands(*subcommands)
    }

    fun execute(args: Array<String>) {
        parser.parse(args)
    }

    fun executeFromString(args: String) {
        execute(args.split(Regex("\\s+")).toTypedArray())
    }
}

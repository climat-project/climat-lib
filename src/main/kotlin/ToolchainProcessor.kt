import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import validation.validate

@OptIn(ExperimentalCli::class, ExperimentalJsExport::class)
@JsExport
class ToolchainProcessor(json: String) {

    private var toolchain = Json.decodeFromString<Toolchain>(json)
    private var parser = ArgParser(toolchain.name, autoTerminate = false)

    init {
        validate(toolchain)
        val subcommands = toolchain.children.orEmpty().map {
            ToolchainSubcommand(it)
        }.toTypedArray()
        parser.subcommands(*subcommands)
    }

    fun execute(args: Array<String>) = parser.parse(args)

    //    fun execute(args: List<String>) = execute(args.toTypedArray())
    fun executeFromString(args: String) = execute(args.split(Regex("\\s+")).toTypedArray())
}

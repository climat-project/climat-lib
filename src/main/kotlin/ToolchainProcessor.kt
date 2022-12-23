import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import validation.validate

@OptIn(ExperimentalCli::class)
class ToolchainProcessor(private val toolchain: Toolchain) {

    private val parser = ArgParser(toolchain.name, autoTerminate = false)

    init {
        validate(toolchain)
        val subcommands = toolchain.children.orEmpty().map {
            ToolchainSubcommand(it)
        }.toTypedArray()
        parser.subcommands(*subcommands)
    }

    fun execute(args: Array<String>) {
        parser.parse(args)
    }
}

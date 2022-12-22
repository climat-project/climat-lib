import kotlinx.cli.CLIEntity
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand

@OptIn(ExperimentalCli::class)
class ToolchainSubcommand(private val toolchain: Toolchain) :
    Subcommand(toolchain.name, toolchain.description ?: "") {

    private val arguments = toolchain.parameters.orEmpty().map {
        val type = toolchainParameterTypeToCliArgType(it.type)
        if (it.optional) {
            option(type, it.name, it.shorthand, it.description)
        }
        else {
            argument(type, it.name, it.description)
        }
    }

    init {
        val subcommands =
            toolchain.children.orEmpty().map { ToolchainSubcommand(it) }.toTypedArray()
        this.subcommands(*subcommands)
    }

    override fun execute() {
        println("executed ${toolchain.name}")
    }
}
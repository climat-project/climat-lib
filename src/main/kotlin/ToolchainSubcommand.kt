
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand

@OptIn(ExperimentalCli::class)
class ToolchainSubcommand(private val toolchain: Toolchain) :
    Subcommand(toolchain.name, toolchain.description ?: "") {

    private var executed = false
    private val arguments = toolchain.parameters.orEmpty().associate {
        val type = toolchainParameterTypeToCliArgType(it.type)
        it.name to if (it.optional) {
            option(type, it.name, it.shorthand, it.description)
        } else {
            argument(type, it.name, it.description)
        }
    }
    private val toolchainSubcommands =
        toolchain.children.orEmpty().map { ToolchainSubcommand(it) }.toTypedArray()

    init {
        autoTerminate = false
        this.subcommands(*toolchainSubcommands)
    }

    override fun execute() {
        val executedChild = toolchainSubcommands.find { it.executed }
        if (executedChild == null) {

            child_process.exec(toolchain.action)
            println("executed ${toolchain.name}")
            println("fullcommandname: $fullCommandName")
        } else {
            executedChild.executed = false
        }
        executed = true
    }
}

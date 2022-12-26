import domain.ParameterWithValue
import domain.Toolchain
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import template.getActualCommand

@OptIn(ExperimentalCli::class)
internal class ToolchainSubcommand(
    private val toolchain: Toolchain,
    private val handler: (String) -> Unit,
    upperScopeParams: Map<String, ParameterWithValue> = emptyMap()
) :
    Subcommand(toolchain.name, toolchain.description ?: "") {

    private var executed = false

    private val params = upperScopeParams + toolchain.parsedParameters.associate {
        val type = toolchainParameterTypeToCliArgType(it.type)
        it.name to
            ParameterWithValue(
                it,
                if (it.optional) {
                    option(type, it.name, it.shorthand, it.description)
                } else {
                    argument(type, it.name, it.description)
                }
            )
    }

    private val toolchainSubcommands =
        toolchain.children.orEmpty().map { ToolchainSubcommand(it, handler, params) }.toTypedArray()

    init {
        autoTerminate = false
        this.subcommands(*toolchainSubcommands)
    }

    override fun execute() {
        val executedChild = toolchainSubcommands.find { it.executed }
        if (executedChild == null) {
            val command = getActualCommand(toolchain.action, params)
            println("Executing `$command`")
            handler(command)
        } else {
            executedChild.executed = false
        }
        executed = true
    }
}

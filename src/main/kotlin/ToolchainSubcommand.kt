import domain.Parameter
import domain.ParameterWithValue
import domain.Toolchain
import kotlinx.cli.ArgType
import kotlinx.cli.CLIEntity
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import template.getActualCommand

@OptIn(ExperimentalCli::class)
internal class ToolchainSubcommand(
    private val toolchain: Toolchain,
    private val handler: (String) -> Unit,
    upperScopeParams: Map<String, ParameterWithValue> = emptyMap(),
    upperScopeParamDefaults: Map<String, String> = emptyMap()
) :
    Subcommand(toolchain.name, toolchain.description ?: "") {

    private var executed = false
    private val paramDefaults = upperScopeParamDefaults + toolchain.paramDefaults.orEmpty()
    private val params = upperScopeParams + toolchain.parsedParameters.associate {
        it.name to ParameterWithValue(
            it,
            cliArgument(it)
        )
    }

    private fun cliArgument(it: Parameter): CLIEntity<out Any> =
        if (it.optional) {
            when (it.type) {
                Toolchain.Type.Arg -> {
                    option(
                        ArgType.String,
                        it.name,
                        it.shorthand,
                        it.description
                    ).default(paramDefaults[it.name] ?: "")
                }

                Toolchain.Type.Flag -> {
                    option(
                        ArgType.Boolean,
                        it.name,
                        it.shorthand,
                        it.description
                    ).default(false)
                }
            }
        } else {
            val type = toolchainParameterTypeToCliArgType(it.type)
            argument(type, it.name, it.description)
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
            val command = getActualCommand(toolchain.parsedAction, params)
            handler(command)
        } else {
            executedChild.executed = false
        }
        executed = true
    }
}

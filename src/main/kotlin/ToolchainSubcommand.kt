import child_process.ExecSyncOptions
import kotlinx.cli.CLIEntity
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import template.getActualCommand

@OptIn(ExperimentalCli::class)
class ToolchainSubcommand(
    private val toolchain: Toolchain,
    upperScopeParams: Map<String, CLIEntity<out Comparable<*>?>> = emptyMap()
) :
    Subcommand(toolchain.name, toolchain.description ?: "") {

    private var executed = false

    private val params = upperScopeParams + toolchain.parameters.orEmpty().associate {
        val type = toolchainParameterTypeToCliArgType(it.type)
        it.name to if (it.optional) {
            option(type, it.name, it.shorthand, it.description)
        } else {
            argument(type, it.name, it.description)
        }
    }

    private val toolchainSubcommands =
        toolchain.children.orEmpty().map { ToolchainSubcommand(it, params) }.toTypedArray()

    private fun getParamValueMap(): Map<String, String> =
        params.map { it.key to it.value.value.toString() }.toMap()

    init {
        autoTerminate = false
        this.subcommands(*toolchainSubcommands)
    }

    override fun execute() {
        val executedChild = toolchainSubcommands.find { it.executed }
        if (executedChild == null) {
            val command = getActualCommand(toolchain.action, getParamValueMap())
            println("Executing `$command`")

            val options: dynamic = object {}
            options["stdio"] = arrayOf("ignore", "inherit", "inherit")
            // Suppressing because normal API doesn't work
            @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
            child_process.execSync(command, options as ExecSyncOptions)
        } else {
            executedChild.executed = false
        }
        executed = true
    }
}

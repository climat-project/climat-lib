
import domain.eachAlias
import domain.ref.Constant
import domain.ref.ParamDefinition
import domain.ref.Ref
import domain.ref.RefWithValue
import domain.refs
import domain.toolchain.Toolchain
import kotlinx.cli.ArgType
import kotlinx.cli.CLIEntity
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import template.getActualCommand

@OptIn(ExperimentalCli::class)
internal class ToolchainCommand(
    private val toolchain: Toolchain,
    private val handler: (parsedAction: String, context: Toolchain) -> Unit,
    upperScopeRefs: Map<String, RefWithValue> = emptyMap(),
    upperScopeDefaults: Map<String, String> = emptyMap()
) :
    Subcommand(toolchain.name, toolchain.description) {

    private var executed = false
    private val defaults = upperScopeDefaults + toolchain.parameterDefaults
    private val params = upperScopeRefs + toolchain.refs.associate {
        it.name to RefWithValue(
            it,
            when (it) {
                is ParamDefinition -> {
                    cliArgument(it).let {
                        {
                            it.delegate.value.toString()
                        }
                    }
                }

                is Constant -> {
                    { it.value }
                }

                else -> {
                    throw Exception("Not supported")
                }
            }
        )
    }

    private fun cliArgument(it: ParamDefinition): CLIEntity<out Any> =
        if (it.optional) {
            when (it.type) {
                Ref.Type.Arg -> {
                    option(
                        ArgType.String,
                        it.name,
                        it.shorthand,
                        it.description
                    ).default(defaults[it.name] ?: emptyString())
                }

                Ref.Type.Flag -> {
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
        toolchain.children.flatMap { child ->
            child.eachAlias.map { toolchain ->
                ToolchainCommand(toolchain, handler, params)
            }
        }.toTypedArray()

    init {
        autoTerminate = false
        this.subcommands(*toolchainSubcommands)
    }

    override fun execute() {
        val executedChild = toolchainSubcommands.find { it.executed }
        if (executedChild == null) {
            val command = getActualCommand(toolchain.action, params)
            handler(command, toolchain)
        } else {
            executedChild.executed = false
        }
        executed = true
    }
}
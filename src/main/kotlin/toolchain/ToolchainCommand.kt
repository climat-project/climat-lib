package toolchain
import domain.action.ActionValueBase
import domain.action.CustomScriptActionValue
import domain.action.NoopActionValue
import domain.action.ScopeParamsActionValue
import domain.action.TemplateActionValue
import domain.eachAlias
import domain.ref.Constant
import domain.ref.ParamDefinition
import domain.ref.Ref
import domain.ref.RefWithValue
import domain.refs
import domain.toolchain.Toolchain
import emptyString
import kotlinx.cli.ArgType
import kotlinx.cli.CLIEntity
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import toolchainParameterTypeToCliArgType

@OptIn(ExperimentalCli::class)
internal class ToolchainCommand(
    private val toolchain: Toolchain,
    private val handler: (parsedAction: ActionValueBase<*>, context: Toolchain) -> Unit,
    upperScopeRefs: Map<String, RefWithValue> = emptyMap(),
    upperScopeDefaults: Map<String, String> = emptyMap()
) :
    Subcommand(toolchain.name, toolchain.description) {

    private var executed = false
    private val defaults = upperScopeDefaults + toolchain.parameterDefaults
    private val currentScopeRefs = toolchain.refs.fold(emptyList<RefWithValue>()) { acc, it ->
        val refWithValue = RefWithValue(
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
                    { it.value.str(acc) }
                }

                else -> {
                    throw Exception("Not supported")
                }
            }
        )
        acc + listOf(refWithValue)
    }
    private val params = upperScopeRefs + currentScopeRefs.associateBy { it.ref.name }

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
            val act = toolchain.action
            setActualCommand(act, params.values)
            handler(act, toolchain)
        } else {
            executedChild.executed = false
        }
        executed = true
    }

    private fun setActualCommand(
        action: ActionValueBase<*>,
        values: Collection<RefWithValue>
    ) {
        when (action) {
            is TemplateActionValue -> {
                action.value = action.template.str(values)
            }
            is CustomScriptActionValue -> {
                action.value = values.associate { it.ref.name to it.value }
            }
            is ScopeParamsActionValue -> {
                action.value = values.associate { it.ref.name to it.value }
            }
            is NoopActionValue -> {
                // By definition, do nothing
            }
            else -> throw Exception("Type `${action::class}` not supported")
        }
    }
}

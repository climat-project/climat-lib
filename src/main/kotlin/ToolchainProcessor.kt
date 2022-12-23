import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli

@OptIn(ExperimentalCli::class)
class ToolchainProcessor(private val toolchain: Toolchain) {

    private val actionRe = Regex("\\\$\\(([\\w\\.]*)\\)")
    private val parser = ArgParser(toolchain.name, autoTerminate = false)

    init {
        val errors = validate(toolchain)
        if (errors.any()) {
            throw Exception(errors.joinToString(", "))
        }

        val subcommands = toolchain.children.orEmpty().map {
            ToolchainSubcommand(it)
        }.toTypedArray()
        parser.subcommands(*subcommands)
    }

    private fun validate(current: Toolchain = toolchain, paramsDefinedAbove: Set<String> = emptySet()): Sequence<String>  {
        val definedParams = paramsDefinedAbove + current.parameters.orEmpty().map { it.name }.toSet()
        val undefinedParams =
            actionRe.findAll(current.action)
                .flatMap { it.groupValues.drop(1) }
                .distinct()
                .filter { !definedParams.contains(it) }

        return undefinedParams.map {
            "Error compiling ${current.name}: Undefined parameter: $it"
        } + (current.children?.flatMap {
            validate(it, definedParams)
        } ?: emptyList()).asSequence()
    }

    public fun execute(args: Array<String>) {
        println("commandName: ${parser.parse(args).commandName}")
    }

}
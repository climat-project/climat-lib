import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli

@OptIn(ExperimentalCli::class)
class ToolchainProcessor(private val toolchain: Toolchain) {

    data class ValidationEntry(val message: String,
        val type: ValidationEntryType) {
        enum class ValidationEntryType {
            Warning,
            Error
        }
    }

    private val actionRe = Regex("\\\$\\(([\\w.]+)(?::([^ ()]+))?\\)")
    private val parser = ArgParser(toolchain.name, autoTerminate = false)

    init {
        val validations = validate(toolchain)
        val warnings = validations.filter { it.type == ValidationEntry.ValidationEntryType.Warning }
            .map { it.message }
        if (warnings.any()) {
            println(warnings.joinToString(", "))
        }

        val errors = validations.filter { it.type == ValidationEntry.ValidationEntryType.Error }
                                .map { it.message }
        if (errors.any()) {
            throw Exception(errors.joinToString(", "))
        }

        val subcommands = toolchain.children.orEmpty().map {
            ToolchainSubcommand(it)
        }.toTypedArray()
        parser.subcommands(*subcommands)
    }

    private fun validate(current: Toolchain = toolchain,
                         paramsDefinedAbove: Set<Toolchain.Parameter> = emptySet()): Sequence<ValidationEntry>  {

        // TODO extract each validation in own class
        val scopeParams = (paramsDefinedAbove + current.parameters.orEmpty()).groupBy { it.name }

        val regexMatches = actionRe.findAll(current.action)
        // Undefined params
        val undefinedParamsValidations =
            regexMatches
                .map { it.groupValues[1] }
                .distinct()
                .filter { !scopeParams.contains(it) }
                .map {
                    ValidationEntry("Error compiling `${current.name}`: Parameter `$it` is not defined in the current scope",
                        ValidationEntry.ValidationEntryType.Error)
                }

        // Duplicate children names
        val duplicateChildrenNamesValidations =
            current.children
                .orEmpty()
                .groupBy { it.name }
                .asSequence()
                .filter { (_, v) -> v.size >= 2 }
                .map { (k, _) -> ValidationEntry("Error compiling `${current.name}`: Duplicate child name `$k`",
                    ValidationEntry.ValidationEntryType.Error) }

        // Duplicate param names
        val duplicateParamNamesValidations =
            current.parameters
                  .orEmpty()
                  .groupBy { it.name }
                  .asSequence()
                  .filter { (_, v) -> v.size >= 2 }
                  .map { (k, _) -> ValidationEntry("Error compiling `${current.name}`: Duplicate parameter `$k`",
                      ValidationEntry.ValidationEntryType.Error) }

        // Validate flag mappings
        val flagMappingsValidations =
            regexMatches.filter { println(it.groups); it.groups[2] != null }
                        .map { it.groupValues[1] }
                        .mapNotNull { scopeParams[it]?.last() }
                        .filter { it.type != Toolchain.Type.bool }
                        .map {
                            ValidationEntry("Error compiling `${current.name}`: " +
                                    "Parameter `${it.name}` is used as a flag mapping, and should have type ${Toolchain.Type.bool.name}",
                                ValidationEntry.ValidationEntryType.Error)
                        }

        val unusedVariablesWarnings =
            scopeParams.values.asSequence()
                .filter { it.size >= 2 }
                .map {
                    ValidationEntry("Warning: Parameter `${it.first().name}` is shadowed",
                        ValidationEntry.ValidationEntryType.Warning)
                }

        // TODO: Warn about unused variables

        return unusedVariablesWarnings +
               flagMappingsValidations +
               duplicateChildrenNamesValidations +
               duplicateParamNamesValidations +
               undefinedParamsValidations +
               (current.children?.flatMap {
                   validate(it, scopeParams.values.flatten().toSet())
               } ?: emptyList()).asSequence()
    }

    fun execute(args: Array<String>) {
        parser.parse(args)
    }

}
import domain.action.Action
import domain.toolchain.RootToolchain
import domain.toolchain.Toolchain
import parser.decodeCliDsl
import validation.computeValidations
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import validation.validate as _validate

@OptIn(ExperimentalJsExport::class)
@JsExport
class ToolchainProcessor {
    companion object {
        fun validate(toolchain: RootToolchain) = computeValidations(toolchain).toList().toTypedArray()
        fun parse(cliDsl: String): RootToolchain =
            decodeCliDsl(cliDsl)
    }

    @JsName("createFromJsonString")
    constructor(cliDsl: String, actionHandler: (parsedAction: Action, context: Toolchain) -> Unit, skipValidation: Boolean = false) :
        this(decodeCliDsl(cliDsl), actionHandler, skipValidation)

    @JsName("create")
    constructor(toolchain: RootToolchain, actionHandler: (parsedAction: Action, context: Toolchain) -> Unit, skipValidation: Boolean = false) {
        if (!skipValidation)
            _validate(toolchain)

        this.parser = ToolchainCommand(toolchain, actionHandler)
    }

    private val parser: ToolchainCommand

    fun execute(args: Array<String>) {
        parser.parse(args)
    }

    @JsName("executeFromString")
    fun execute(args: String) {
        execute(args.split(Regex("\\s+")).toTypedArray())
    }
}

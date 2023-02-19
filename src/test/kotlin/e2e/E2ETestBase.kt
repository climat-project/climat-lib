package e2e

import com.climat.library.domain.action.TemplateActionValue
import com.climat.library.toolchain.ToolchainProcessor
import kotlin.test.assertEquals

abstract class E2ETestBase {

    private fun exec(args: String, cliDsl: String): String? {
        var ans: TemplateActionValue? = null
        ToolchainProcessor(cliDsl, { act, _ ->
            ans = act as TemplateActionValue
        }).execute(args)
        return ans?.value
    }

    protected fun String.assertResults(vararg commandToResult: Pair<String, String?>) {
        commandToResult.forEach {
            (command, expectedResult) ->
            val actualResult = exec(command, this)
            assertEquals(expectedResult?.trim(), actualResult?.trim(), "Unexpected result for command `$command`")
        }
    }
}

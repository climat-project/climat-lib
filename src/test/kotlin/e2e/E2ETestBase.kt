package e2e

import com.climat.library.domain.action.TemplateActionValue
import com.climat.library.toolchain.execute
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.fail

abstract class E2ETestBase {

    private fun exec(args: String, cliDsl: String): String? {
        var ans: TemplateActionValue? = null
        execute(args, cliDsl, { act, _ ->
            ans = act as TemplateActionValue
        })
        return ans?.value
    }

    protected fun String.assertResults(vararg commandToResult: Pair<String, String?>) {
        commandToResult.forEach { (command, expectedResult) ->
            val actualResult = exec(command, this)
            assertEquals(expectedResult?.trim(), actualResult?.trim(), "Unexpected result for command `$command`")
        }
    }

    protected fun <T : Throwable> String.assertThrows(vararg commantToResult: Pair<String, (T) -> Unit>) {
        commantToResult.forEach { (command, exceptionHandler) ->
            val ex = assertFails("Command `$command` did not throw any exception") {
                exec(command, this)
            } as? T
            if (ex != null) {
                exceptionHandler(ex)
            } else {
                fail("Command `$command` did not throw the expected exception type")
            }
        }
    }
}

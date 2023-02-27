package e2e

import com.climat.library.commandParser.execute
import com.climat.library.domain.action.TemplateActionValue
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.fail

abstract class E2ETestBase {

    private fun exec(args: String, cliDsl: String): String? {
        var ans: TemplateActionValue? = null
        val argv =
            if (args.isEmpty()) emptyArray()
            else args.split(" ").toTypedArray()
        execute(argv, cliDsl, { act, _ ->
            ans = act as TemplateActionValue
        })
        return ans?.value
    }

    protected fun String.assertResults(vararg commandToResult: Pair<String, String?>): String {
        commandToResult.forEach { (command, expectedResult) ->
            val actualResult = exec(command, this)
            assertEquals(expectedResult?.trim(), actualResult?.trim(), "Unexpected result for command `$command`")
        }
        return this
    }

    protected fun <T : Throwable> String.assertThrows(
        vararg commandToResult: Pair<String, (T) -> Unit>,
        print: Boolean = false
    ): String {
        commandToResult.forEach { (command, exceptionHandler) ->
            val ex = assertFails("Command `$command` did not throw any exception") {
                val res = exec(command, this)
            } as? T
            if (ex != null) {
                if (print) println(ex.message)
                exceptionHandler(ex)
            } else {
                fail("Command `$command` did not throw the expected exception type")
            }
        }
        return this
    }

    protected fun <T : Throwable> assertMessageContains(vararg tokens: String): (T) -> Unit {
        return {
            val message = it.message.orEmpty()
            tokens.forEach { assertContains(message, it) }
        }
    }
}

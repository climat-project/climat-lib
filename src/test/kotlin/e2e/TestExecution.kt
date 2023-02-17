package e2e
import com.climat.library.domain.action.TemplateActionValue
import com.climat.library.toolchain.ToolchainProcessor
import kotlin.test.Test
import kotlin.test.assertContentEquals

class TestExecution {

    private val cliDsl = """
    sub cli-alias-aggregator {
      const C1 = "constantValue"
      const C2 = true
      action "echo root action"
      
      children [
        sub new(interactive?: flag) {
          action "echo 'abcd'"
          children [
            sub template(param1?: arg = "default", param2?: arg) {
              action "echo '${'$'}(interactive)' '${'$'}(param1)' ${'$'}(interactive:--interactiveSwitch) ${'$'}(param1:--mapped)"
            }
          ]
        }
        sub renew {
          action "echo 'qwe' ${'$'}(C1:--c) ${'$'}(C1) ${'$'}(C2:--switch)"
        }
        sub remove(force?: flag) {
          action "echo 'what ever'"
        }
        sub export(type?: flag) {
          action "echo 'abcd'"
        }
        sub noop {}
      ]
      
    }"""

    private fun exec(args: String): String? {
        var ans: TemplateActionValue? = null
        ToolchainProcessor(cliDsl, { act, _ ->
            ans = act as TemplateActionValue
        }).execute(args)
        return ans?.value
    }

    @Test
    fun testHappyFlow() {
        val executed = listOf(
            "new",
            "new --interactive template --param1 abc",
            "new template --param2 we",
            "new --interactive template --param2 we --param1 nondefault",
            "renew",
            "",
            "noop"
        )
            .map(::exec)
            .toTypedArray()
        println(executed)
        assertContentEquals(
            arrayOf(
                "echo 'abcd'",
                "echo 'true' 'abc' --interactiveSwitch --mapped=abc",
                "echo 'false' 'default' --mapped=default",
                "echo 'true' 'nondefault' --interactiveSwitch --mapped=nondefault",
                "echo 'qwe' --c=constantValue constantValue --switch",
                "echo root action",
                null
            ),
            executed
        )
    }
}

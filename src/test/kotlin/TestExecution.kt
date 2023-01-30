
import domain.TemplateActionValue
import kotlin.test.Test
import kotlin.test.assertContentEquals

class TestExecution {

    private val cliDsl = """
    cli-alias-aggregator {
      const C1 = "constantValue"
      const C2 = true
      
      children [
        new(interactive?: flag) {
          action "echo 'abcd'"
          children [
            template(param1?: arg = "default", param2?: arg) {
              action "echo '$(interactive)' '$(param1)' $(interactive:--interactiveSwitch) $(param1:--mapped)"
            }
          ]
        },
        renew {
          action "echo 'qwe' $(C1:--c) $(C1) $(C2:--switch)"
        },
        remove(force?: flag) {
          action "echo 'what ever'"
        },
        export(type?: flag) {
          action "echo 'abcd'"
        },
      ]
    }"""

    private fun exec(args: String): String {
        var ans: TemplateActionValue? = null
        ToolchainProcessor(cliDsl, { act, _ ->
            ans = act as TemplateActionValue
        }).execute(args)
        return ans!!.value!!
    }

    @Test
    fun testHappyFlow() {
        val executed = listOf(
            "new",
            "new --interactive template --param1 abc",
            "new template --param2 we",
            "new --interactive template --param2 we --param1 nondefault",
            "renew"
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
                "echo 'qwe' --c=constantValue constantValue --switch"
            ),
            executed
        )
    }
}

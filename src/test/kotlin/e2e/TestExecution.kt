package e2e
import kotlin.test.Test

class TestExecution : E2ETestBase() {

    private val cliDsl = """
    cli-alias-aggregator {
      const C1 = "constantValue"
      const C2 = true
      action "echo root action"
      
      
      sub new(interactive: flag) {
        action "echo 'abcd'"
        
        sub template(param1: arg? = "default", param2: arg?) {
          action "echo '$(interactive)' '$(param1)' $(interactive:--interactiveSwitch) $(param1:--mapped)"
        }
      }
      sub renew {
        action "echo 'qwe' $(C1:--c) $(C1) $(C2:--switch)"
      }
      sub remove(force: flag) {
        action "echo 'what ever'"
      }
      sub export(type: flag) {
        action "echo 'abcd'"
      }
      sub noop {}
    }"""

    @Test
    fun testHappyFlow() {
        cliDsl.assertResults(
            "new" to
                "echo 'abcd'",

            "new --interactive template --param1 abc" to
                "echo 'true' 'abc' --interactiveSwitch --mapped=abc",

            "new template --param2 we" to
                "echo 'false' 'default' --mapped=default",

            "new --interactive template --param2 we --param1 nondefault" to
                "echo 'true' 'nondefault' --interactiveSwitch --mapped=nondefault",

            "renew" to
                "echo 'qwe' --c=constantValue constantValue --switch",

            "" to
                "echo root action",

            "noop" to
                null
        )
    }
}

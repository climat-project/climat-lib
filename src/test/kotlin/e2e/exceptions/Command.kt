package e2e.exceptions

import e2e.E2ETestBase
import kotlin.test.Test

class Command : E2ETestBase() {
    @Test
    fun test() {
        """
            hello-world(location l: arg) {
                action "echo Hello World from $(location)"

                sub child {
                    action "echo Hello Child from $(location)"
                }
                
                sub child2(param1: arg, param2: arg) {
                    
                }
            }
        """
            .assertThrows<Exception>(
                "" to assertMessageContains("hello-world", "location"),
                "qwe er" to assertMessageContains("er", "child", "child2"),
                "--location qwe child" to assertMessageContains("qwe", "child", "child2"),
                "-l childbb" to assertMessageContains("childbb", "child", "child2")
            )
    }
}

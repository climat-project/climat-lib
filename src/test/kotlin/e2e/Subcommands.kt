package e2e

import kotlin.test.Test

class Subcommands : E2ETestBase() {

    @Test
    fun noop() {
        """
            my-toolchain {
                sub noop {}
            }
        """
            .assertResults(
                "" to null,
                "noop" to null
            )
    }

    @Test
    fun aliases() {
        """
            my-toolchain {

                @aliases(ct child) // Defines one or more aliases
                @alias(cld)          // Defines one alias
                sub child-toolchain {
                    action "echo 'Child'"
                }

            }
        """
            .assertResults(
                "child-toolchain" to "echo 'Child'",
                "child" to "echo 'Child'",
                "ct" to "echo 'Child'",
                "cld" to "echo 'Child'"
            )
    }
}

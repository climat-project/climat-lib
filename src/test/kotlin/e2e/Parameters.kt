package e2e

import kotlin.test.Ignore
import kotlin.test.Test

class Parameters : E2ETestBase() {

    @Test
    fun scope() {
        """
            hello-world(location l: arg) {
                action "echo Hello World from $(location)"

                sub child {
                    action "echo Hello Child from $(location)"
                }
            }
        """
            .assertResults(
                "Cluj-Napoca" to
                    "echo Hello World from Cluj-Napoca",

                "Cluj-Napoca child" to
                    "echo Hello Child from Cluj-Napoca"
            )
    }

    @Test
    fun optionals() {
        """
            hello-world(location l?: arg) {
                action "echo Hello World from $(location)"
            }
        """
            .assertResults(
                "" to "echo Hello World from",
                "--location Cluj-Napoca" to "echo Hello World from Cluj-Napoca",
                "-l Cluj-Napoca" to "echo Hello World from Cluj-Napoca"
            )
    }

    @Test
    fun defaults() {
        """
            hello-world(location l?: arg = "the other side") {
                action "echo Hello World from $(location)"
            }
        """
            .assertResults(
                "" to "echo Hello World from the other side",
                "--location Cluj-Napoca" to "echo Hello World from Cluj-Napoca"
            )
    }

    @Test
    @Ignore // "Functionality not supported by kotlinx.cli"
    fun overridingDefaults() {
        """
            hello-world(location l?: arg = "the other side") {
                action "echo Hello World from $(location)"

                sub child {
                    override default location = "this side"
                    action "echo Hello Child from $(location)"
                }
            }
        """
            .assertResults(
                "" to "echo Hello World from the other side",

                "--location Cluj-Napoca" to "echo Hello World from Cluj-Napoca",

                "child" to "echo Hello Child from this side",

                "--location Cluj-Napoca child" to "echo Hello Child from Cluj-Napoca"
            )
    }
}

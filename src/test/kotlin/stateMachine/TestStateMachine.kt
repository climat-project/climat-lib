package stateMachine

import com.climat.library.stateMachine.stateMachine
import kotlin.test.DefaultAsserter.assertEquals
import kotlin.test.Test
import kotlin.test.assertFails

class TestStateMachine {

    @Test
    fun test() {
        val sm = stateMachine<String, String> ("Solid") {
            state("Solid") {
                on("Melting") {
                    transitionTo("Liquid")
                }
            }
            state("Liquid") {
                on("Vaporising") {
                    transitionTo("Gas")
                }
                on("Freezing") {
                    transitionTo("Solid")
                }
            }
            state("Gas") {
                on("Condensing") {
                    transitionTo("Liquid")
                }
            }
        }

        assertEquals("Invalid init state", "Solid", sm.current)
        sm.next("Melting")
        assertEquals("Invalid transition state", "Liquid", sm.current)
        sm.next("Freezing")
        assertEquals("Invalid transition state", "Solid", sm.current)
        sm.next("Melting")
        assertEquals("Invalid transition state", "Liquid", sm.current)
        sm.next("Vaporising")
        assertEquals("Invalid transition state", "Gas", sm.current)
        sm.next("Condensing")
        assertEquals("Invalid transition state", "Liquid", sm.current)

        assertFails("Didn't error at undefined event") {
            sm.next("Turning into plasma")
        }
        assertEquals("Invalid transition state after error", "Liquid", sm.current)
    }
}

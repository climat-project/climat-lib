package validation

import com.climat.library.parser.dsl.decodeCliDsl
import com.climat.library.validation.validations.ValidationCode
import utils.assertContainsInMessages
import utils.getValidationMessages
import kotlin.test.Test

class TestUselessToolchain {
    private val toolchain = """
        sub root(param1?: flag, param2?: arg) {
            children [
                sub root_child() { action "dummy $(param1) $(param2) $(param_2) $(undef) $(undef2)" }
                sub useless_child(param1?: flag, param_2?: arg, param3?: arg) {}
            ]
        }
    """

    @Test
    fun test() {
        val validationResults = decodeCliDsl(toolchain).getValidationMessages(ValidationCode.UselessToolchain)
        assertContainsInMessages(
            validationResults,
            "useless_child",
        )
    }
}

package validation

import domain.decodeFromString
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test

class TestUselessToolchain {
    private val toolchain = """
        root(param1?: flag, param2?: arg) {
            children [
                root_child() { action "dummy $(param1) $(param2) $(param_2) $(undef) $(undef2)" },
                useless_child(param1?: flag, param_2?: arg, param3?: arg) {}
            ]
        }
    """

    @Test
    fun test() {
        val validationResults = decodeFromString(toolchain).getValidationMessages(ValidationCode.UselessToolchain)
        assertContainsInMessages(
            validationResults,
            "useless_child",
        )
    }
}

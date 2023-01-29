package validation

import domain.decodeFromString
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test

class TestUndefinedParams {
    private val toolchain = """
        root(param1?: flag, param2?: arg) {
            action "dummy $(undef)"
            children [
                root_child(param1?: flag, param_2?:arg, param3?: arg) {
                    action "dummy $(param1) $(param2) $(param_2) $(undef) $(undef2)"
                }
            ]
        }
    """
    @Test
    fun test() {
        val validationResults = decodeFromString(toolchain).getValidationMessages(ValidationCode.UndefinedParams)
        assertContainsInMessages(
            validationResults,
            "undef",
            "undef",
            "undef2"
        )
    }
}

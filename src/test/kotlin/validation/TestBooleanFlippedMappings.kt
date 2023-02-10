package validation

import parser.dsl.decodeCliDsl
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test

class TestBooleanFlippedMappings {
    private val toolchain = """
        root(arg1: arg, arg2: arg, arg3: flag) {
            action "dummy command $(!arg1)"
            children [
                child1() { action "dummy2 command $(!arg2) $(!arg3)" }
            ]
        }
    """

    @Test
    fun test() {
        val validationResults = decodeCliDsl(toolchain).getValidationMessages(ValidationCode.BooleanFlippedMappings)
        assertContainsInMessages(
            validationResults,
            "arg1",
            "arg2"
        )
    }
}

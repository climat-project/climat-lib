package validation

import parser.decodeCliDsl
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test

class TestShadowedParams {
    private val toolchain = """
        root(param1: arg, param2: arg) {
            children [
                child1(param1: arg, param3: arg) {
                    children [
                        grandchild(param1?: flag, param3?: flag) {}
                    ]
                },
                child2(param2?: flag, param4?: flag) {}
            ]
        }
    """

    @Test
    fun test() {
        val validationResults = decodeCliDsl(toolchain).getValidationMessages(ValidationCode.ShadowedParams)
        assertContainsInMessages(
            validationResults,
            "param1",
            "param3",
            "param2"
        )
    }
}

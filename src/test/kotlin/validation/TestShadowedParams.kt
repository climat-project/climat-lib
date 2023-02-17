package validation

import com.climat.library.parser.dsl.decodeCliDsl
import com.climat.library.validation.validations.ValidationCode
import utils.assertContainsInMessages
import utils.getValidationMessages
import kotlin.test.Test

class TestShadowedParams {
    private val toolchain = """
        root(param1: arg, param2: arg) {
            children [
                sub child1(param1: arg, param3: arg) {
                    children [
                        sub grandchild(param1?: flag, param3?: flag) {}
                    ]
                }
                sub child2(param2?: flag, param4?: flag) {}
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

package validation

import com.climat.library.dslParser.dsl.decodeCliDsl
import com.climat.library.validation.validations.ValidationCode
import utils.assertContainsInMessages
import utils.getValidationMessages
import kotlin.test.Test

class ShadowedParams {
    private val toolchain = """
        root(param1: arg, param2: arg) {
            sub child1(param1: arg, param3: arg) {
                    sub grandchild(param1: flag, param3: flag) {}
            }
            sub child2(param2: flag, param4: flag) {}
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

package validation

import parser.decodeCliDsl
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test

class TestFlagMappedTwice {
    private val toolchain = """
        root(root_param1: arg, root_param2: arg) {
            action "dummy command $(root_param) $(root_param1:dummyCommandParam) $(root_param2:dummyCommandParam)"
            children [
                child {
                    action "dummy2 command $(r:cparam) $(c:cparam)"
                }
            ]
        }
    """

    @Test
    fun test() {
        val validationResults = decodeCliDsl(toolchain).getValidationMessages(ValidationCode.FlagMappedTwice)
        assertContainsInMessages(
            validationResults,
            "dummyCommandParam",
            "cparam"
        )
    }
}

package validation

import ToolchainDto
import kotlinx.serialization.json.JsonPrimitive
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test

class TestFlagMappedTwice {
    private val toolchain = ToolchainDto(
        name = "root",
        parameters = arrayOf(
            "req:arg:root_param1:descr",
            "req:arg:root_param2:descr"
        ),
        action = JsonPrimitive("dummy command $(root_param) $(root_param1:dummyCommandParam) $(root_param2:dummyCommandParam)"),
        children = arrayOf(
            ToolchainDto(
                name = "child",
                action = JsonPrimitive("dummy2 command $(r:cparam) $(c:cparam)")
            )
        )
    )

    @Test
    fun test() {
        val validationResults = toolchain.getValidationMessages(ValidationCode.FlagMappedTwice)
        assertContainsInMessages(
            validationResults,
            "dummyCommandParam",
            "cparam"
        )
    }
}

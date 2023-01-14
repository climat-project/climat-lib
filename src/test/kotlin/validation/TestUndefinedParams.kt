package validation

import ToolchainDto
import kotlinx.serialization.json.JsonPrimitive
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test

class TestUndefinedParams {
    private val toolchain =
        ToolchainDto(
            name = "root",
            action = JsonPrimitive("dummy $(undef)"),
            parameters = arrayOf("opt:flag:param1", "opt:arg:param2"),
            children = arrayOf(
                ToolchainDto(
                    name = "root_child",
                    action = JsonPrimitive("dummy $(param1) $(param2) $(param_2) $(undef) $(undef2)"),
                    parameters = arrayOf("opt:flag:param1", "opt:arg:param_2", "opt:arg:param3")
                )
            )
        )
    @Test
    fun test() {
        val validationResults = toolchain.getValidationMessages(ValidationCode.UndefinedParams)
        assertContainsInMessages(
            validationResults,
            "undef",
            "undef",
            "undef2"
        )
    }
}

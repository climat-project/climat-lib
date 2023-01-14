package validation

import ToolchainDto
import kotlinx.serialization.json.JsonPrimitive
import utils.getValidations
import validation.validations.ValidationCode
import kotlin.test.Test
import kotlin.test.assertEquals

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
        val validationResults = toolchain.getValidations(ValidationCode.UndefinedParams)
        assertEquals(3, validationResults.count())
    }
}

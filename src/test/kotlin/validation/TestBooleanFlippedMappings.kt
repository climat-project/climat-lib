package validation

import ToolchainDto
import kotlinx.serialization.json.JsonPrimitive
import utils.getValidations
import validation.validations.ValidationCode
import kotlin.test.Test
import kotlin.test.assertEquals

class TestBooleanFlippedMappings {
    val toolchain = ToolchainDto(
        name =  "root",
        parameters = arrayOf(
            "req:arg:arg1:descr",
            "req:arg:arg2:descr",
            "req:flag:arg3:descr"
        ),
        action = JsonPrimitive("dummy command $(!arg1)"),
        children = arrayOf(
            ToolchainDto(
                name = "child1",
                action = JsonPrimitive("dummy2 command $(!arg2) $(!arg3)")
            )
        )
    )

    @Test
    fun test() {
        val validationResults = toolchain.getValidations(ValidationCode.BooleanFlippedMappings)
        assertEquals(2, validationResults.count())
    }
}
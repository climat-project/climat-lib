package validation

import domain.dto.ToolchainDto
import kotlinx.serialization.json.JsonPrimitive
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test

class TestDuplicateParamNames {
    private val toolchain =
        ToolchainDto(
            name = "root",
            action = JsonPrimitive("dummy action"),
            parameters = arrayOf("opt:flag:param", "opt:arg:param"),
            children = arrayOf(
                ToolchainDto(
                    name = "root_child",
                    action = JsonPrimitive("dummy action 2"),
                    parameters = arrayOf("opt:flag:param1", "opt:arg:param2", "opt:arg:param3")
                ),
                ToolchainDto(
                    name = "root_child_2",
                    action = JsonPrimitive("dummy action 3"),
                    children = arrayOf(
                        ToolchainDto(
                            name = "root_grandchild",
                            action = JsonPrimitive("dummy action 5")
                        ),
                        ToolchainDto(
                            name = "root_grandchild_2",
                            action = JsonPrimitive("dummy action 6")
                        )
                    )
                ),
                ToolchainDto(
                    name = "root_child_3",
                    action = JsonPrimitive("dummy action 4"),
                    parameters = arrayOf("opt:flag:param1", "opt:arg:param2", "opt:arg:param1")
                )
            )
        )
    @Test
    fun test() {
        val validationResults = toolchain.getValidationMessages(ValidationCode.DuplicateRefNames)
        assertContainsInMessages(
            validationResults,
            "param",
            "param1"
        )
    }
}

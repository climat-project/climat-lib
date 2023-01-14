package validation

import ToolchainDto
import kotlinx.serialization.json.JsonPrimitive
import utils.assertContainsInMessages
import utils.getValidations
import validation.validations.ValidationCode
import kotlin.test.Test
import kotlin.test.assertEquals

class TestDuplicateChildrenName {
    private val toolchain =
        ToolchainDto(
            name = "root",
            action = JsonPrimitive("dummy action"),
            children = arrayOf(
                ToolchainDto(
                    name = "root_child",
                    action = JsonPrimitive("dummy action 2")
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
                            name = "root_grandchild",
                            action = JsonPrimitive("dummy action 6")
                        )
                    )
                ),
                ToolchainDto(
                    name = "root_child",
                    action = JsonPrimitive("dummy action 4"),
                    children = arrayOf(
                        ToolchainDto(
                            name = "root_grandchild",
                            action = JsonPrimitive("dummy action 5")
                        ),
                        ToolchainDto(
                            name = "root_grandchild",
                            action = JsonPrimitive("dummy action 6")
                        ),
                        ToolchainDto(
                            name = "root_child",
                            action = JsonPrimitive("dummy action 6")
                        )
                    )
                )
            )
        )
    @Test
    fun test() {
        val validationResults = toolchain.getValidations(ValidationCode.DuplicateChildrenNames)
        assertContainsInMessages(
            validationResults,
            "root_child",
            "root_grandchild",
            "root_grandchild"
        )
        assertEquals(3, validationResults.count())
    }
}

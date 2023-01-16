package validation

import ToolchainDto
import kotlinx.serialization.json.JsonPrimitive
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test

class TestDuplicateToolchainNameOrAlias {
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
                            action = JsonPrimitive("dummy action 6"),
                            children = arrayOf(
                                ToolchainDto(
                                    name = "grand_grand_child",
                                    aliases = arrayOf("grand_grand_child_alias", "grand_grand_child_2")
                                ),
                                ToolchainDto(
                                    name = "grand_grand_child_2",
                                    aliases = arrayOf("grand_grand_child_2")
                                ),
                                ToolchainDto(
                                    name = "grand_grand_child_3",
                                    aliases = arrayOf("grand_grand_child_3_alias", "xylophone", "grand_grand_child_3_alias")
                                ),
                                ToolchainDto(
                                    name = "grand_grand_child_4",
                                    aliases = arrayOf("xylophone")
                                ),
                            )
                        )
                    )
                )
            )
        )
    @Test
    fun test() {
        val validationResults = toolchain.getValidationMessages(ValidationCode.DuplicateToolchainNamesOrAliases)
        assertContainsInMessages(
            validationResults,
            "root_child",
            "root_grandchild",
            "root_grandchild",
            "grand_grand_child_2",
            "grand_grand_child_3_alias",
            "xylophone"
        )
    }
}

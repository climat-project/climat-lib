package validation

import domain.dto.DescendantToolchainDto
import kotlinx.serialization.json.JsonPrimitive
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test

class TestDuplicateToolchainNameOrAlias {
    private val toolchain =
        DescendantToolchainDto(
            name = "root",
            action = JsonPrimitive("dummy action"),
            children = arrayOf(
                DescendantToolchainDto(
                    name = "root_child",
                    action = JsonPrimitive("dummy action 2")
                ),
                DescendantToolchainDto(
                    name = "root_child_2",
                    action = JsonPrimitive("dummy action 3"),
                    children = arrayOf(
                        DescendantToolchainDto(
                            name = "root_grandchild",
                            action = JsonPrimitive("dummy action 5")
                        ),
                        DescendantToolchainDto(
                            name = "root_grandchild",
                            action = JsonPrimitive("dummy action 6")
                        )
                    )
                ),
                DescendantToolchainDto(
                    name = "root_child",
                    action = JsonPrimitive("dummy action 4"),
                    children = arrayOf(
                        DescendantToolchainDto(
                            name = "root_grandchild",
                            action = JsonPrimitive("dummy action 5")
                        ),
                        DescendantToolchainDto(
                            name = "root_grandchild",
                            action = JsonPrimitive("dummy action 6")
                        ),
                        DescendantToolchainDto(
                            name = "root_child",
                            action = JsonPrimitive("dummy action 6"),
                            children = arrayOf(
                                DescendantToolchainDto(
                                    name = "grand_grand_child",
                                    aliases = arrayOf("grand_grand_child_alias", "grand_grand_child_2")
                                ),
                                DescendantToolchainDto(
                                    name = "grand_grand_child_2",
                                    aliases = arrayOf("grand_grand_child_2")
                                ),
                                DescendantToolchainDto(
                                    name = "grand_grand_child_3",
                                    aliases = arrayOf("grand_grand_child_3_alias", "xylophone", "grand_grand_child_3_alias")
                                ),
                                DescendantToolchainDto(
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

package validation

import ToolchainDto
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test

class TestAncestorSubcommandWithSameName {
    private val toolchain =
        ToolchainDto(
            name = "root",
            children = arrayOf(
                ToolchainDto(
                    name = "child",
                    children = arrayOf(
                        ToolchainDto(
                            name = "root",
                            children = arrayOf(
                                ToolchainDto(
                                    name = "child"
                                )
                            )
                        )
                    )
                )
            )
        )

    @Test
    fun test() {
        val validationResults = toolchain.getValidationMessages(ValidationCode.AncestorSubcommandWithSameName)
        assertContainsInMessages(
            validationResults,
            "root",
            "child"
        )
    }
}

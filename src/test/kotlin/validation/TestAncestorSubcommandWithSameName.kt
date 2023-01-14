package validation

import ToolchainDto
import utils.getValidations
import validation.validations.ValidationCode
import kotlin.test.Test
import kotlin.test.assertEquals

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
        val validationResults = toolchain.getValidations(ValidationCode.AncestorSubcommandWithSameName)
        assertEquals(2, validationResults.count())
    }
}

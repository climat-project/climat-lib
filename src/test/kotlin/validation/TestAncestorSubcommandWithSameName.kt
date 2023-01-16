package validation

import domain.dto.DescendantToolchainDto
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test

class TestAncestorSubcommandWithSameName {
    private val toolchain =
        DescendantToolchainDto(
            name = "root",
            children = arrayOf(
                DescendantToolchainDto(
                    name = "child",
                    children = arrayOf(
                        DescendantToolchainDto(
                            name = "root",
                            children = arrayOf(
                                DescendantToolchainDto(
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

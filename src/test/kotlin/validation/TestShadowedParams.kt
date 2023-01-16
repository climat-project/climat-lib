package validation

import domain.dto.DescendantToolchainDto
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test

class TestShadowedParams {
    private val toolchain = DescendantToolchainDto(
        name = "root",
        parameters = arrayOf(
            "req:arg:param1:descr",
            "req:arg:param2:descr"
        ),
        children = arrayOf(
            DescendantToolchainDto(
                name = "child1",
                parameters = arrayOf(
                    "req:arg:param1:descr",
                    "req:arg:param3:descr"
                ),
                children = arrayOf(
                    DescendantToolchainDto(
                        name = "grandchild",
                        parameters = arrayOf(
                            "opt:flag:param1:descr2",
                            "opt:flag:param3:descr2"
                        ),
                    )
                )
            ),
            DescendantToolchainDto(
                name = "child2",
                parameters = arrayOf(
                    "opt:flag:param2",
                    "opt:flag:param4"
                )
            )
        )
    )

    @Test
    fun test() {
        val validationResults = toolchain.getValidationMessages(ValidationCode.ShadowedParams)
        assertContainsInMessages(
            validationResults,
            "param1",
            "param3",
            "param2"
        )
    }
}

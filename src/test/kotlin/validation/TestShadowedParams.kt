package validation

import domain.dto.ToolchainDto
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test

class TestShadowedParams {
    private val toolchain = ToolchainDto(
        name = "root",
        parameters = arrayOf(
            "req:arg:param1:descr",
            "req:arg:param2:descr"
        ),
        children = arrayOf(
            ToolchainDto(
                name = "child1",
                parameters = arrayOf(
                    "req:arg:param1:descr",
                    "req:arg:param3:descr"
                ),
                children = arrayOf(
                    ToolchainDto(
                        name = "grandchild",
                        parameters = arrayOf(
                            "opt:flag:param1:descr2",
                            "opt:flag:param3:descr2"
                        ),
                    )
                )
            ),
            ToolchainDto(
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

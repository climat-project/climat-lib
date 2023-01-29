package validation

import domain.dto.DescendantToolchainDto
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test

class TestDefaultForUndefinedParam {
    private val toolchain = DescendantToolchainDto(
        name = "root",

        parameters = arrayOf(
            "req:arg:rootParam:descr"
        ),

        children = arrayOf(
            DescendantToolchainDto(
                name = "child",
                parameters = arrayOf(
                    "req:arg:param1:descr",
                    "req:flag:param2:descr",
                ),
                paramDefaults = mapOf(
                    "param1" to "str_default_1",
                    "param2" to "bool_default",
                    "param3" to "str_default_2",
                    "param4" to "str_default_3",
                    "rootParam" to "str_default_4",
                    "rootParam2" to "str_default_5"
                )
            )
        )
    )

    @Test
    fun test() {
        val validationResults = toolchain.getValidationMessages(ValidationCode.DefaultForUndefinedParam)
        assertContainsInMessages(
            validationResults,
            "param3",
            "param4",
            "rootParam2"
        )
    }
}

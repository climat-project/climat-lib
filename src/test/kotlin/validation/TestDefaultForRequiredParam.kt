package validation

import ToolchainDto
import utils.assertContainsInMessages
import utils.getValidations
import validation.validations.ValidationCode
import kotlin.test.Test

class TestDefaultForRequiredParam {
    private val toolchain = ToolchainDto(
        name = "root",

        parameters = arrayOf(
            "req:arg:rootParam:descr"
        ),

        children = arrayOf(
            ToolchainDto(
                name = "child",
                parameters = arrayOf(
                    "req:arg:param1:descr",
                    "req:flag:param2:descr",
                    "opt:arg:param3:descr",
                    "opt:flag:param4:descr"
                ),
                paramDefaults = mapOf(
                    "param1" to "str_default_1",
                    "param2" to "bool_default",
                    "param3" to "str_default_2",
                    "param4" to "str_default_3",
                    "rootParam" to "str_default_4"
                )
            )
        )
    )

    @Test
    fun test() {
        val validationResults = toolchain.getValidations(ValidationCode.DefaultForRequiredParam)
        assertContainsInMessages(
            validationResults,
            listOf("child", "rootParam"),
            listOf("child", "param1"),
            listOf("child", "param2")
        )
    }
}

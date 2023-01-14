package validation

import ToolchainDto
import utils.getValidations
import validation.validations.ValidationCode
import kotlin.test.Test
import kotlin.test.assertEquals

class TestDefaultForFlag {
    private val toolchain = ToolchainDto(
        name = "root",
        children = arrayOf(
            ToolchainDto(
                name = "child",
                parameters = arrayOf(
                    "req:arg:param1:descr",
                    "req:flag:param2:descr",
                    "req:flag:param3:descr",
                    "req:flag:param4:descr",
                    "req:flag:param5:descr"
                ),
                paramDefaults = mapOf(
                    "param1" to "str_default",
                    "param2" to "true",
                    "param3" to "random"
                ),
                children = arrayOf(
                    ToolchainDto(
                        name = "grandchild",
                        paramDefaults = mapOf(
                            "param4" to "false"
                        )
                    )
                )
            )
        )
    )

    @Test
    fun test() {
        val validationResults = toolchain.getValidations(ValidationCode.DefaultForFlag)
        assertEquals(3, validationResults.count())
    }
}

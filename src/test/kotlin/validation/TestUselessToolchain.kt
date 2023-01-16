package validation

import domain.dto.DescendantToolchainDto
import kotlinx.serialization.json.JsonPrimitive
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test

class TestUselessToolchain {
    private val toolchain =
        DescendantToolchainDto(
            name = "root",
            parameters = arrayOf("opt:flag:param1", "opt:arg:param2"),
            children = arrayOf(
                DescendantToolchainDto(
                    name = "root_child",
                    action = JsonPrimitive("dummy $(param1) $(param2) $(param_2) $(undef) $(undef2)"),
                    parameters = arrayOf("opt:flag:param1", "opt:arg:param_2", "opt:arg:param3")
                ),
                DescendantToolchainDto(
                    name = "useless_child",
                    parameters = arrayOf("opt:flag:param1", "opt:arg:param_2", "opt:arg:param3")
                )
            )
        )
    @Test
    fun test() {
        val validationResults = toolchain.getValidationMessages(ValidationCode.UselessToolchain)
        assertContainsInMessages(
            validationResults,
            "useless_child",
        )
    }
}

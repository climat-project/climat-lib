import domain.convert
import kotlinx.serialization.json.JsonPrimitive
import validation.ValidationResult
import validation.computeValidations
import kotlin.test.Test
import kotlin.test.assertEquals

class TestValidation {

    private fun getErrors(toolchain: ToolchainDto): Sequence<ValidationResult> =
        computeValidations(convert(toolchain)).filter { it.type == ValidationResult.ValidationEntryType.Error }

    @Test
    fun testDuplicateChildrenName() {
        val toolchain =
            ToolchainDto(
                name = "root",
                action = JsonPrimitive("dummy action"),
                children = arrayOf(
                    ToolchainDto(
                        name = "root_child",
                        action = JsonPrimitive("dummy action 2")
                    ),
                    ToolchainDto(
                        name = "root_child_2",
                        action = JsonPrimitive("dummy action 3"),
                        children = arrayOf(
                            ToolchainDto(
                                name = "root_grandchild",
                                action = JsonPrimitive("dummy action 5")
                            ),
                            ToolchainDto(
                                name = "root_grandchild",
                                action = JsonPrimitive("dummy action 6")
                            )
                        )
                    ),
                    ToolchainDto(
                        name = "root_child",
                        action = JsonPrimitive("dummy action 4"),
                        children = arrayOf(
                            ToolchainDto(
                                name = "root_grandchild",
                                action = JsonPrimitive("dummy action 5")
                            ),
                            ToolchainDto(
                                name = "root_grandchild",
                                action = JsonPrimitive("dummy action 6")
                            ),
                            ToolchainDto(
                                name = "root_child",
                                action = JsonPrimitive("dummy action 6")
                            )
                        )
                    )
                )
            )

        val validationResults = getErrors(toolchain)
        assertEquals(3, validationResults.count())
    }

    @Test
    fun testDuplicateParamNames() {
        val toolchain =
            ToolchainDto(
                name = "root",
                action = JsonPrimitive("dummy action"),
                parameters = arrayOf("opt:flag:param", "opt:arg:param"),
                children = arrayOf(
                    ToolchainDto(
                        name = "root_child",
                        action = JsonPrimitive("dummy action 2"),
                        parameters = arrayOf("opt:flag:param1", "opt:arg:param2", "opt:arg:param3")
                    ),
                    ToolchainDto(
                        name = "root_child_2",
                        action = JsonPrimitive("dummy action 3"),
                        children = arrayOf(
                            ToolchainDto(
                                name = "root_grandchild",
                                action = JsonPrimitive("dummy action 5")
                            ),
                            ToolchainDto(
                                name = "root_grandchild_2",
                                action = JsonPrimitive("dummy action 6")
                            )
                        )
                    ),
                    ToolchainDto(
                        name = "root_child_3",
                        action = JsonPrimitive("dummy action 4"),
                        parameters = arrayOf("opt:flag:param1", "opt:arg:param2", "opt:arg:param1")
                    )
                )
            )

        val validationResults = getErrors(toolchain)
        assertEquals(2, validationResults.count())
    }

    @Test
    fun testUndefinedParams() {
        val toolchain =
            ToolchainDto(
                name = "root",
                action = JsonPrimitive("dummy $(undef)"),
                parameters = arrayOf("opt:flag:param1", "opt:arg:param2"),
                children = arrayOf(
                    ToolchainDto(
                        name = "root_child",
                        action = JsonPrimitive("dummy $(param1) $(param2) $(param_2) $(undef) $(undef2)"),
                        parameters = arrayOf("opt:flag:param1", "opt:arg:param_2", "opt:arg:param3")
                    )
                )
            )

        val validationResults = getErrors(toolchain)
        assertEquals(3, validationResults.count())
    }
}

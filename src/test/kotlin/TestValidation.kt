import validation.ValidationResult
import validation.computeValidations
import kotlin.test.Test
import kotlin.test.assertEquals

class TestValidation {

    private fun getErrors(toolchain: Toolchain): Sequence<ValidationResult> =
        computeValidations(toolchain).filter { it.type == ValidationResult.ValidationEntryType.Error }

    @Test
    fun testDuplicateChildrenName() {
        val toolchain =
            Toolchain(
                name = "root",
                action = "dummy action",
                children = listOf(
                    Toolchain(
                        name = "root_child",
                        action = "dummy action 2"
                    ),
                    Toolchain(
                        name = "root_child_2",
                        action = "dummy action 3",
                        children = listOf(
                            Toolchain(
                                name = "root_grandchild",
                                action = "dummy action 5"
                            ),
                            Toolchain(
                                name = "root_grandchild",
                                action = "dummy action 6"
                            )
                        )
                    ),
                    Toolchain(
                        name = "root_child",
                        action = "dummy action 4",
                        children = listOf(
                            Toolchain(
                                name = "root_grandchild",
                                action = "dummy action 5"
                            ),
                            Toolchain(
                                name = "root_grandchild",
                                action = "dummy action 6"
                            ),
                            Toolchain(
                                name = "root_child",
                                action = "dummy action 6"
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
            Toolchain(
                name = "root",
                action = "dummy action",
                parameters = listOf("opt:flag:param", "opt:arg:param"),
                children = listOf(
                    Toolchain(
                        name = "root_child",
                        action = "dummy action 2",
                        parameters = listOf("opt:flag:param1", "opt:arg:param2", "opt:arg:param3")
                    ),
                    Toolchain(
                        name = "root_child_2",
                        action = "dummy action 3",
                        children = listOf(
                            Toolchain(
                                name = "root_grandchild",
                                action = "dummy action 5"
                            ),
                            Toolchain(
                                name = "root_grandchild_2",
                                action = "dummy action 6"
                            )
                        )
                    ),
                    Toolchain(
                        name = "root_child_3",
                        action = "dummy action 4",
                        parameters = listOf("opt:flag:param1", "opt:arg:param2", "opt:arg:param1")
                    )
                )
            )

        val validationResults = getErrors(toolchain)
        assertEquals(2, validationResults.count())
    }

    @Test
    fun testFlagMappings() {
        val toolchain =
            Toolchain(
                name = "root",
                action = "grep $(intParam:-v) $(intParam:-i) $(boolParam:-I)",
                parameters = listOf("opt:flag:boolParam", "opt:arg:intParam"),
                children = listOf(
                    Toolchain(
                        name = "root_child",
                        action = "dummy --file $(param3) $(param2:--quiet) $(param1:--verbose)",
                        parameters = listOf("opt:flag:param1", "opt:arg:param2", "opt:arg:param3")
                    )
                )
            )

        val validationResults = getErrors(toolchain)
        print(validationResults.joinToString())
        assertEquals(3, validationResults.count())
    }

    @Test
    fun testUndefinedParams() {
        val toolchain =
            Toolchain(
                name = "root",
                action = "dummy $(undef)",
                parameters = listOf("opt:flag:param1", "opt:arg:param2"),
                children = listOf(
                    Toolchain(
                        name = "root_child",
                        action = "dummy $(param1) $(param2) $(param_2) $(undef) $(undef2)",
                        parameters = listOf("opt:flag:param1", "opt:arg:param_2", "opt:arg:param3")
                    )
                )
            )

        val validationResults = getErrors(toolchain)
        assertEquals(3, validationResults.count())
    }
}

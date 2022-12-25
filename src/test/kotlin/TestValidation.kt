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
                parameters = listOf(
                    Toolchain.Parameter(name = "param", type = Toolchain.Type.bool),
                    Toolchain.Parameter(name = "param", type = Toolchain.Type.int)
                ),
                children = listOf(
                    Toolchain(
                        name = "root_child",
                        action = "dummy action 2",
                        parameters = listOf(
                            Toolchain.Parameter(name = "param1", type = Toolchain.Type.bool),
                            Toolchain.Parameter(name = "param2", type = Toolchain.Type.int),
                            Toolchain.Parameter(name = "param3", type = Toolchain.Type.int)
                        )
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
                        parameters = listOf(
                            Toolchain.Parameter(name = "param1", type = Toolchain.Type.bool),
                            Toolchain.Parameter(name = "param2", type = Toolchain.Type.int),
                            Toolchain.Parameter(name = "param1", type = Toolchain.Type.int)
                        )
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
                parameters = listOf(
                    Toolchain.Parameter(name = "boolParam", type = Toolchain.Type.bool),
                    Toolchain.Parameter(name = "intParam", type = Toolchain.Type.int)
                ),
                children = listOf(
                    Toolchain(
                        name = "root_child",
                        action = "dummy --file $(param3) $(param2:--quiet) $(param1:--verbose)",
                        parameters = listOf(
                            Toolchain.Parameter(name = "param1", type = Toolchain.Type.bool),
                            Toolchain.Parameter(name = "param2", type = Toolchain.Type.double),
                            Toolchain.Parameter(name = "param3", type = Toolchain.Type.string)
                        )
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
                parameters = listOf(
                    Toolchain.Parameter(name = "param1", type = Toolchain.Type.bool),
                    Toolchain.Parameter(name = "param2", type = Toolchain.Type.int)
                ),
                children = listOf(
                    Toolchain(
                        name = "root_child",
                        action = "dummy $(param1) $(param2) $(param_2) $(undef) $(undef2)",
                        parameters = listOf(
                            Toolchain.Parameter(name = "param1", type = Toolchain.Type.bool),
                            Toolchain.Parameter(name = "param_2", type = Toolchain.Type.int),
                            Toolchain.Parameter(name = "param3", type = Toolchain.Type.int)
                        )
                    )
                )
            )

        val validationResults = getErrors(toolchain)
        assertEquals(3, validationResults.count())
    }
}

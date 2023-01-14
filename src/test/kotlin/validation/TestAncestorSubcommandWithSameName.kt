package validation

import ToolchainDto
import utils.getWarnings
import kotlin.test.Test
import kotlin.test.assertEquals

class TestAncestorSubcommandWithSameName {
    private val toolchain =
        ToolchainDto(
            name = "root",
            children = arrayOf(
                ToolchainDto(
                    name = "child",
                    children = arrayOf(
                        ToolchainDto(
                            name = "root",
                            children = arrayOf(
                                ToolchainDto(
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
        val validationResults = toolchain.getWarnings()
        assertEquals(2, validationResults.count())
    }
}

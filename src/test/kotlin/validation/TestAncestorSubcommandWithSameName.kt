package validation

import parser.dsl.decodeCliDsl
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test

class TestAncestorSubcommandWithSameName {
    private val toolchain = """
        root {
            children [
                child {
                    children[
                        root() {
                            children [
                                child {}
                            ]
                        }
                    ]
                }
            ]
        }
    """

    @Test
    fun test() {
        val validationResults = decodeCliDsl(toolchain).getValidationMessages(ValidationCode.AncestorSubcommandWithSameName)
        assertContainsInMessages(
            validationResults,
            "root",
            "child"
        )
    }
}

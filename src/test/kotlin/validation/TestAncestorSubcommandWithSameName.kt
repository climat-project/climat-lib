package validation

import com.climat.library.parser.dsl.decodeCliDsl
import com.climat.library.validation.validations.ValidationCode
import utils.assertContainsInMessages
import utils.getValidationMessages
import kotlin.test.Test

class TestAncestorSubcommandWithSameName {
    private val toolchain = """
        sub root {
            children [
                sub child {
                    children[
                        sub root() {
                            children [
                                sub child {}
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

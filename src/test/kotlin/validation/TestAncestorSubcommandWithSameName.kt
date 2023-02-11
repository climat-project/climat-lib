package validation

import com.climat.library.parser.dsl.decodeCliDsl
import com.climat.library.validation.validations.ValidationCode
import utils.assertContainsInMessages
import utils.getValidationMessages
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

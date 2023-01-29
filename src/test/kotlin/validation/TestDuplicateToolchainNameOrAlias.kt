package validation

import domain.decodeFromString
import utils.assertContainsInMessages
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Ignore
import kotlin.test.Test

// TODO unignore after implementing aliases
@Ignore
class TestDuplicateToolchainNameOrAlias {

    private val toolchain = """
        root {
            action "dummy action"
            
            children [
                root_child() { action "dummy action 2" }
                root_child2() {
                    action = "dummy action 3"
                    children [
                        root_grandchild() { action "dummy action 5" }
                        root_grandchild() { action "dummy action 6" }
                    ]
                
                },
                root_child() {
                    action = "dummy action 4"
                    children [
                        root_grandchild() { action "dummy action 5" }
                        root_grandchild() { action "dummy action 6" }
                        root_child { 
                            action "dummy action 6" 
                            children [
                                grand_grand_child() {}
                                grand_grand_child_2() {}
                                grand_grand_child_3() {}
                                grand_grand_child_4() {}
                            ]
                        }
                    ]
                
                }
            ]
            
        }
    """
    @Test
    fun test() {
        val validationResults = decodeFromString(toolchain).getValidationMessages(ValidationCode.DuplicateToolchainNamesOrAliases)
        assertContainsInMessages(
            validationResults,
            "root_child",
            "root_grandchild",
            "root_grandchild",
            "grand_grand_child_2",
            "grand_grand_child_3_alias",
            "xylophone"
        )
    }
}

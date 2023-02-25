package validation

import com.climat.library.dslParser.dsl.decodeCliDsl
import com.climat.library.validation.validations.ValidationCode
import utils.assertContainsInMessages
import utils.getValidationMessages
import kotlin.test.Test

class AllowUnmatchedOnNonLeaf {

    private val toolchain = """
        my-toolchain {
         
            @allow-unmatched
            sub child1 {
                sub grandchild {
                }
            }
            
            @allow-unmatched
            sub child2 { }
           
        }
    """

    @Test
    fun test() {
        val validationResults =
            decodeCliDsl(toolchain).getValidationMessages(ValidationCode.AllowUnmatchedOnNonLeaf)
        assertContainsInMessages(
            validationResults,
            "child1"
        )
    }
}

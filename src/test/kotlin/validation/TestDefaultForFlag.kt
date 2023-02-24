package validation

import com.climat.library.dslParser.dsl.decodeCliDsl
import com.climat.library.validation.validations.ValidationCode
import utils.getValidationMessages
import kotlin.test.Test
import kotlin.test.assertEquals

class TestDefaultForFlag {

    private val toolchain = """
        root() {
            sub child(param1: arg = "str_default",
                param2: flag = true,
                param3: flag = "random",
                param4: flag,
                param5: flag) {
               
            }
        }
    """

    @Test
    fun test() {
        val validationResults = decodeCliDsl(toolchain).getValidationMessages(ValidationCode.DefaultForFlag)
        assertEquals(2, validationResults.count())
    }
}

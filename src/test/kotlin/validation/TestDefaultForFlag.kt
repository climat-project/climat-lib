package validation

import com.climat.library.parser.dsl.decodeCliDsl
import com.climat.library.validation.validations.ValidationCode
import utils.getValidationMessages
import kotlin.test.Test
import kotlin.test.assertEquals

class TestDefaultForFlag {

    private val toolchain = """
        root() {
            children [
                sub child(param1: arg = "str_default",
                      param2: flag = true,
                      param3: flag = "random",
                      param4: flag,
                      param5: flag) {
                      
                    children [
                        sub grandchild() {
                            override default param1 = "str_default_2"
                            override default param4 = false
                        }
                    ]
                  
                }
            ]
        }
    """

    @Test
    fun test() {
        val validationResults = decodeCliDsl(toolchain).getValidationMessages(ValidationCode.DefaultForFlag)
        assertEquals(3, validationResults.count())
    }
}

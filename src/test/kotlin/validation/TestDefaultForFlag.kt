package validation

import domain.decodeFromString
import utils.getValidationMessages
import validation.validations.ValidationCode
import kotlin.test.Test
import kotlin.test.assertEquals

class TestDefaultForFlag {

    private val toolchain = """
        root() {
            children [
                child(param1: arg = "str_default",
                      param2: flag = true,
                      param3: flag = "random",
                      param4: flag,
                      param5: flag) {
                      
                    children [
                        grandchild() {
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
        val validationResults = decodeFromString(toolchain).getValidationMessages(ValidationCode.DefaultForFlag)
        assertEquals(3, validationResults.count())
    }
}

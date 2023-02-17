package validation

import com.climat.library.parser.dsl.decodeCliDsl
import com.climat.library.validation.validations.ValidationCode
import utils.assertContainsInMessages
import utils.getValidationMessages
import kotlin.test.Test

class TestDefaultForUndefinedParam {
    private val toolchain = """
        root(rootParam: arg) {
            children [
                sub child(param1: arg = "str_default_1", param2: flag = "bool_default") {
                    override default param3 = "str_default_2"
                    override default param4 = "str_default_3"
                    override default rootParam = "str_default_4"
                    override default rootParam2 = "str_default_5"
                }
            ]
        }
    """

    @Test
    fun test() {
        val validationResults = decodeCliDsl(toolchain).getValidationMessages(ValidationCode.DefaultForUndefinedParam)
        assertContainsInMessages(
            validationResults,
            "param3",
            "param4",
            "rootParam2"
        )
    }
}

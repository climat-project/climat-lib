package validation

import com.climat.library.parser.dsl.decodeCliDsl
import com.climat.library.validation.validations.ValidationCode
import utils.assertContainsInMessages
import utils.getValidationMessages
import kotlin.test.Test

// TODO delegate to parser
class TestDefaultForRequiredParam {

    private val toolchain = """
        sub root(rootParam: arg) {
            children [
                sub child(param1: arg = "str_default_1",
                      param2: flag = "bool_default",
                      param3?: arg = "str_default_2",
                      param4?: flag = "str_default_3") {
                    override default rootParam = "str_default_4"
                }
            ]
        }
    """

    @Test
    fun test() {
        val validationResults = decodeCliDsl(toolchain).getValidationMessages(ValidationCode.DefaultForRequiredParam)
        assertContainsInMessages(
            validationResults,
            listOf("child", "rootParam"),
            listOf("child", "param1"),
            listOf("child", "param2")
        )
    }
}

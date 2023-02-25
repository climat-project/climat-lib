package validation

import com.climat.library.dslParser.dsl.decodeCliDsl
import com.climat.library.validation.validations.ValidationCode
import utils.assertContainsInMessages
import utils.getValidationMessages
import kotlin.test.Test

// TODO delegate to parser
class TestDefaultForRequiredParam {

    private val toolchain = """
        root(rootParam: arg) {
            sub child(param1: arg = "str_default_1",
                  param2: flag,
                  param3: arg? = "str_default_2",
                  param4: flag) {
            }
        }
    """

    @Test
    fun test() {
        val validationResults = decodeCliDsl(toolchain).getValidationMessages(ValidationCode.DefaultForRequiredParam)
        assertContainsInMessages(
            validationResults,
            listOf("child", "param1")
        )
    }
}

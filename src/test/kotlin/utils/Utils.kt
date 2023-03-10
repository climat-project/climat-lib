package utils

import com.climat.library.domain.toolchain.RootToolchain
import com.climat.library.validation.computeValidations
import com.climat.library.validation.validations.ValidationCode
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal fun RootToolchain.getValidationMessages(code: ValidationCode): List<String> =
    computeValidations(
        this
    ).filter { it.code == code }
        .map { it.message }
        .toList()

internal fun assertContainsInMessages(messages: List<String>, vararg predicates: List<String>) {
    assertEquals(
        predicates.size,
        messages.size,
        "Expected number of validation messages to be <${predicates.size}>. Actual size is <${messages.size}>"
    )
    predicates.zip(messages).forEach { (predicate, message) ->
        predicate.forEach {
            assertTrue(
                it in message,
                "Expected the validation message <$message> to contain <$it>"
            )
        }
    }
}

internal fun assertContainsInMessages(messages: List<String>, vararg predicates: String) {
    assertContainsInMessages(messages, *(predicates.map { listOf(it) }.toTypedArray()))
}

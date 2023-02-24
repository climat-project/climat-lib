package com.climat.library.utils
import com.climat.library.domain.action.NoopActionValue

internal fun <T> not(predicate: (T) -> Boolean): (T) -> Boolean = { it: T -> !predicate(it) }

internal fun emptyString(): String = ""

internal fun newLine(): String = unixNewLine()
internal fun newLines(number: Int) = (1..number).joinToString(emptyString()) { newLine() }

internal fun String?.tpl(template: (String) -> String): String =
    if (this != null) {
        template(this)
    } else {
        emptyString()
    }

internal fun windowsNewLine(): String = "\r\n"
internal fun unixNewLine(): String = "\n"
internal fun macNewLine(): String = "\r"

internal fun String.crossPlatformLineSplit(): List<String> =
    this.split(windowsNewLine(), macNewLine(), unixNewLine())

internal fun noopAction() = NoopActionValue()

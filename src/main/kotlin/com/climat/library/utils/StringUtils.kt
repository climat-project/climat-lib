package com.climat.library.utils

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

internal fun <T> Array<T>.joinToStringIfNotEmpty(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((T) -> CharSequence)? = null
): String =
    if (this.any()) {
        this.joinToString(separator, prefix, postfix, limit, truncated, transform)
    } else emptyString()

internal fun <T> Iterable<T>.joinToStringIfNotEmpty(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((T) -> CharSequence)? = null
): String =
    if (this.any()) {
        this.joinToString(separator, prefix, postfix, limit, truncated, transform)
    } else emptyString()

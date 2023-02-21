package com.climat.library.utils
import com.climat.library.domain.action.NoopActionValue

internal fun <T> not(predicate: (T) -> Boolean): (T) -> Boolean = { it: T -> !predicate(it) }

internal fun emptyString(): String = ""

internal fun newLine(): String = unixNewLine()

internal fun windowsNewLine(): String = "\r\n"
internal fun unixNewLine(): String = "\n"
internal fun macNewLine(): String = "\r"

internal fun String.crossPlatformLineSplit(): List<String> =
    this.split(windowsNewLine(), macNewLine(), unixNewLine())

internal fun <K, V> Map<K, V?>.filterNotNullValues(): Map<K, V> =
    mapNotNull { (key, value) -> value?.let { key to it } }.toMap()

internal fun noopAction() = NoopActionValue()

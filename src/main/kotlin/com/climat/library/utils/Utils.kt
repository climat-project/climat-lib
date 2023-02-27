package com.climat.library.utils

import com.climat.library.domain.action.NoopActionValue

internal fun <T> not(predicate: (T) -> Boolean): (T) -> Boolean = { it: T -> !predicate(it) }

internal fun noopAction() = NoopActionValue()

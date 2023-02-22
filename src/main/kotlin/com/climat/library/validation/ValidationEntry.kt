package com.climat.library.validation

import org.antlr.v4.kotlinruntime.misc.Interval

internal data class ValidationEntry(
    val message: String,
    val sourceMap: Interval? = null
)

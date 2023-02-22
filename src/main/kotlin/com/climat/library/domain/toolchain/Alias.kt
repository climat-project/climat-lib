package com.climat.library.domain.toolchain

import org.antlr.v4.kotlinruntime.misc.Interval

data class Alias(
    val name: String,
    internal val sourceMap: Interval
)

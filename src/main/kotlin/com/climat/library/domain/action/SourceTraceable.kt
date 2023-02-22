package com.climat.library.domain.action

import org.antlr.v4.kotlinruntime.misc.Interval

abstract class SourceTraceable {

    internal abstract val sourceMap: Interval?
}

package com.climat.library.domain.ref

data class RefWithValue<T : Any> internal constructor(
    val ref: Ref,
    val value: T
)

typealias RefWithAnyValue = RefWithValue<*>

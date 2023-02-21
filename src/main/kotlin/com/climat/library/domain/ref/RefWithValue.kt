package com.climat.library.domain.ref

data class RefWithValue internal constructor(
    val ref: Ref,
    val valueGetter: () -> String
) {

    internal constructor(ref: Ref, value: String) : this(ref, { value })

    val value get() = valueGetter()
}

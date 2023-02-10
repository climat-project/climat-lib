package domain.ref

data class RefWithValue internal constructor(
    val ref: Ref,
    val valueGetter: () -> String
) {
    val value get() = valueGetter()
}

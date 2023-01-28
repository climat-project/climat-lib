package domain.ref

data class RefWithValue(
    val ref: Ref,
    val valueGetter: () -> String
) {
    val value get() = valueGetter()
}
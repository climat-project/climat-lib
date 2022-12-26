package domain

import kotlinx.cli.CLIEntity

data class ParameterWithValue(
    val definition: Parameter,
    private val delegate: CLIEntity<out Any>
) {
    val value get() = delegate.value.toString()
}

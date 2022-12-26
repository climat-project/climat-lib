package domain

import kotlinx.cli.CLIEntity

data class ParameterWithValue(
    val definition: Parameter,
    private val delegate: CLIEntity<out Comparable<*>?>
) {
    val value get() = delegate.value.toString()
}

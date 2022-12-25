package domain

import kotlinx.cli.CLIEntity

data class ParameterWithValue(
    val definition: Parameter,
    private val delegate: CLIEntity<out Comparable<*>?>
) {
    private val delegateValue by delegate
    val value get() = delegateValue.toString()
}

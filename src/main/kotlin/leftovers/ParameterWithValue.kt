package leftovers

import domain.ParamDefinition
import kotlinx.cli.CLIEntity

data class ParameterWithValue(
    val definition: ParamDefinition,
    private val delegate: CLIEntity<out Any>
) {
    val value get() = delegate.value.toString()
}

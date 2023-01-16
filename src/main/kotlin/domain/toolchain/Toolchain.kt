package domain.toolchain

import domain.IAction
import domain.ref.Constant
import domain.ref.ParamDefinition

abstract class Toolchain {
    abstract val name: String
    abstract val description: String
    abstract val parameters: Array<ParamDefinition>
    abstract val parameterDefaults: Map<String, String>
    abstract val action: IAction
    abstract val children: Array<DescendantToolchain>
    abstract val constants: Array<Constant>
}

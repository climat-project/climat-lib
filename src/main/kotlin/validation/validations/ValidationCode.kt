package validation.validations

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
enum class ValidationCode {
    AncestorSubcommandWithSameName,
    BooleanFlippedMappings,
    DefaultForFlag,
    DefaultForRequiredParam,
    DefaultForUndefinedParam,
    DuplicateToolchainNamesOrAliases,
    DuplicateRefNames,
    FlagMappedTwice,
    ShadowedParams,
    UndefinedParams,
    UselessToolchain
}

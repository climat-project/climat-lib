package com.climat.library.validation.validations

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
enum class ValidationCode {
    AncestorSubcommandWithSameName,
    BooleanFlippedMappings,
    DefaultForFlag,
    DefaultForRequiredParam,
    DuplicateToolchainNamesOrAliases,
    DuplicateRefNames,
    FlagMappedTwice,
    ShadowedParams,
    UndefinedParams,
    UselessToolchain
}

package com.climat.library.validation.validations

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
enum class ValidationCode {
    AllowUnmatchedOnNonLeaf,
    AncestorSubcommandWithSameName,
    BooleanFlippedMappings,
    DefaultForRequiredParam,
    DuplicateToolchainNamesOrAliases,
    DuplicateRefNames,
    FlagMappedTwice,
    ShadowedParams,
    UndefinedParams,
    UselessToolchain
}

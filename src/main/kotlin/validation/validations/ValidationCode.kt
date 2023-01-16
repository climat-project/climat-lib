package validation.validations

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

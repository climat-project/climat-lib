package validation.validations

enum class ValidationCode {
    AncestorSubcommandWithSameName,
    BooleanFlippedMappings,
    DefaultForFlag,
    DefaultForRequiredParam,
    DefaultForUndefinedParam,
    DuplicateChildrenNames,
    DuplicateRefNames,
    FlagMappedTwice,
    ShadowedParams,
    UndefinedParams
}

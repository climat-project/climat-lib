package validation.validations

enum class ValidationCode {
    AncestorSubcommandWithSameName,
    BooleanFlippedMappings,
    DefaultForFlag,
    DefaultForRequiredParam,
    DefaultForUndefinedParam,
    DuplicateChildrenNames,
    DuplicateReferenceableNames,
    FlagMappedTwice,
    ShadowedParams,
    UndefinedParams
}

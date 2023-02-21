parser grammar DslParser;
options { tokenVocab=DslLexer; }

root: docstring? rootModifiers* IDENTIFIER (LPAREN params? RPAREN)? rootBody EOF;
rootModifiers: MOD_SEAL;
rootBody: LCURLY rootStatements* RCURLY;
rootStatements: sub | action | constDef;

sub: docstring? subModifiers* SUB IDENTIFIER (LPAREN params? RPAREN)? subBody;
// TODO implement functionality for SEALED and SHIFTED modifiers
subModifiers: rootModifiers | MOD_SHIFT | aliasModifier | aliasesModifier;
aliasModifier: MOD_ALIAS LPAREN IDENTIFIER RPAREN;
aliasesModifier: MOD_ALIASES LPAREN IDENTIFIER+ RPAREN;
params: param (COMMA param)* COMMA?;
param: IDENTIFIER paramShort? COLON paramType (EQ literal)?;
paramType: FLAG | argument;
argument: ARGUMENT (QMARK)?;
paramShort: IDENTIFIER;

subBody: LCURLY subStatements* RCURLY;
subStatements: rootStatements | defaultOverride;

action: ACTION_PROP actionValue;
constDef: CONST IDENTIFIER EQ literal;
defaultOverride: OVERRIDE DEFAULT IDENTIFIER EQ literal;

actionValue: stringTemplate | SCOPE_PARAMS | customScript;
customScript: IDENTIFIER? CUSTOM_SCRIPT_BEGIN CustomScript_SCRIPT? CustomScript_END;

literal: stringTemplate | booleanLiteral;
booleanLiteral: TRUE | FALSE;

stringTemplate: DOUBLE_QUOTE entry* Template_CLOSE;
entry: content | interpolation;
content: Template_CONTENT;
interpolation: Template_INTERPOLATION_OPEN Interpolation_NEGATE? Interpolation_IDENTIFIER mapping? Interpolation_RPAREN;
mapping: Interpolation_COLON Interpolation_IDENTIFIER;

docstring: DOCSTRING_BEGIN docstringEntry* Docstring_END;
docstringEntry: Docstring_CONTENT | paramTag;
paramTag: Docstring_AT_PARAM Docstring_IDENTIFIER;


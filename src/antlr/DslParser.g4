parser grammar DslParser;
options { tokenVocab=DslLexer; }

root: docstring? rootModifiers* IDENTIFIER (LPAREN params? RPAREN)? rootBody EOF;
rootModifiers: SEALED;
rootBody: LCURLY rootStatements* RCURLY;
rootStatements: children | action | constDef;

sub: docstring? subModifiers* SUB IDENTIFIER (LPAREN params? RPAREN)? subBody;
// TODO implement functionality for SEALED and SHIFTED modifiers
subModifiers: rootModifiers | SHIFTED;
params: param (COMMA param)* COMMA?;
param: IDENTIFIER ALPHANUMERIC? (QMARK)? COLON paramType (EQ literal)?;
paramType: FLAG | ARGUMENT;

subBody: LCURLY subStatements* RCURLY;
subStatements: rootStatements | aliases | defaultOverride;

children: CHILDREN_PROP LBRAKET sub+ RBRAKET;
action: ACTION_PROP actionValue;
aliases: ALIASES_PROP LBRAKET (IDENTIFIER (COMMA IDENTIFIER)* COMMA? )? RBRAKET;
constDef: CONST IDENTIFIER EQ literal;
defaultOverride: OVERRIDE DEFAULT IDENTIFIER EQ literal;

actionValue: strintTemplate | SCOPE_PARAMS | customScript;
customScript: IDENTIFIER? CUSTOM_SCRIPT_BEGIN CustomScript_SCRIPT? CustomScript_END;

literal: strintTemplate | booleanLiteral;
booleanLiteral: TRUE | FALSE;

strintTemplate: DOUBLE_QUOTE entry* Template_CLOSE;
entry: content | interpolation;
content: Template_CONTENT;
interpolation: Template_INTERPOLATION_OPEN Interpolation_NEGATE? Interpolation_IDENTIFIER mapping? Interpolation_RPAREN;
mapping: Interpolation_COLON Interpolation_IDENTIFIER;

docstring: DOCSTRING_BEGIN docstringEntry* Docstring_END;
docstringEntry: Docstring_CONTENT | paramTag;
paramTag: Docstring_AT_PARAM Docstring_IDENTIFIER;


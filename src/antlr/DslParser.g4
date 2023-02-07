parser grammar DslParser;
options { tokenVocab=DslLexer; }

root: func EOF;

funcModifiers: SEALED | SHIFTED;

func: docstring? funcModifiers* IDENTIFIER (LPAREN params? RPAREN)? funcBody;
params: param (COMMA param)* COMMA?;
param: IDENTIFIER ALPHANUMERIC? (QMARK)? COLON paramType (EQ literal)?;
paramType: FLAG | ARGUMENT;

funcBody: LCURLY funcStatements* RCURLY;
funcStatements: children | action | aliases | constDef | defaultOverride;

children: CHILDREN_PROP LBRAKET func (COMMA func)* COMMA? RBRAKET;
action: ACTION_PROP actionValue;
aliases: ALIASES_PROP LBRAKET (IDENTIFIER (COMMA IDENTIFIER)* COMMA? )? RBRAKET;
constDef: CONST IDENTIFIER EQ literal;
defaultOverride: OVERRIDE DEFAULT IDENTIFIER EQ literal;

actionValue: strintTemplate | SCOPE_PARAMS | customScript;
customScript: IDENTIFIER? CUSTOM_SCRIPT;

literal: strintTemplate | booleanLiteral;
booleanLiteral: TRUE | FALSE;

strintTemplate: DOUBLE_QUOTE entry* Template_CLOSE;
entry: content | interpolation;
content: Template_CONTENT;
interpolation: Template_KACHING Template_LPAREN Interpolation_NEGATE? Interpolation_IDENTIFIER mapping? Interpolation_RPAREN;
mapping: Interpolation_COLON Interpolation_IDENTIFIER;

docstring: DOCSTRING_BEGIN docstringEntry* Docstring_END;
docstringEntry: Docstring_CONTENT | paramTag;
paramTag: Docstring_AT_PARAM Docstring_IDENTIFIER Docstring_WS;


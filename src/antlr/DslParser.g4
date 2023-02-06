parser grammar DslParser;
options { tokenVocab=DslLexer; }

root: func EOF;

funcModifiers: SEALED | SHIFTED;

func: DOCSTRING? funcModifiers* IDENTIFIER (LPAREN params? RPAREN)? funcBody;
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

actionValue: stringLiteral | SCOPE_PARAMS | customScript;
customScript: IDENTIFIER? CUSTOM_SCRIPT;

literal: stringLiteral | booleanLiteral;
stringLiteral: STRING_LITERAL;
booleanLiteral: TRUE | FALSE;

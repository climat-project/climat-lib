parser grammar CliDslParser;
options { tokenVocab=CliDslLexer; }

root: func EOF;

funcModifiers: SEALED | SHIFTED;

func: funcModifiers* NAME (LPAREN params? RPAREN)? funcBody;
params: param (COMMA param)* COMMA?;
param: NAME SHORTHAND? (QMARK)? COLON paramType (EQ literal)?;
paramType: FLAG | ARGUMENT;

funcBody: LCURLY funcStatements* RCURLY;
funcStatements: children | action | aliases | constDef | defaultOverride;

children: CHILDREN_PROP LBRAKET func (COMMA func)* COMMA? RBRAKET;
action: ACTION_PROP actionValue;
aliases: ALIASES_PROP LBRAKET (NAME (COMMA NAME)* COMMA? )? RBRAKET;
constDef: CONST NAME EQ literal;
defaultOverride: OVERRIDE DEFAULT NAME EQ literal;

actionValue: stringLiteral | SCOPE_PARAMS;

literal: stringLiteral | booleanLiteral;
stringLiteral: STRING_LITERAL;
booleanLiteral: TRUE | FALSE;



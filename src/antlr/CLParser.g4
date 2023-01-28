parser grammar CLParser;
options { tokenVocab=CLLexer; }

func: NAME LPAREN params? RPAREN funcBody;
params: param (COMMA param)*;
param: NAME COLON paramType (EQ literal)?;
paramType: OPTION | ARGUMENT;

funcBody: LCURLY funcStatements* RCURLY;
funcStatements: children | action | aliases | constDef;

children: CHILDREN_PROP LBRAKET func (COMMA func)* RBRAKET;
action: ACTION_PROP DOUBLE_QUOTE ANYTHING DOUBLE_QUOTE;
aliases: ALIASES_PROP LBRAKET (NAME (COMMA NAME)*)? RBRAKET;
constDef: CONST NAME EQ literal;

literal: stringLiteral | booleanLiteral;
stringLiteral: DOUBLE_QUOTE .*? DOUBLE_QUOTE;
booleanLiteral: TRUE | FALSE;



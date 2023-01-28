parser grammar CLParser;

options { tokenVocab=CLLexer; }

program: func;

func: name LPAREN params RPAREN funcBody;

name: ID;

params: param (COMMA param)*;

param: name SEMI paramType;

paramType: OPTION | ARGUMENT;

funcBody: LCURLY (props EOL)* RCURLY;

props: children | action;

children: L func (COMMA func)* R;

action: ACTION_PROP DOUBLE_QUOTE ANYTHING DOUBLE_QUOTE;



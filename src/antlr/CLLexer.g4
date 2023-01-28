// DELETE THIS CONTENT IF YOU PUT COMBINED GRAMMAR IN Parser TAB
lexer grammar CLLexer;

EQ : '=' ;
COMMA : ',' ;
COLON : ':' ;
LPAREN : '(' ;
RPAREN : ')' ;
LCURLY : '{' ;
RCURLY : '}' ;
LBRAKET: '[';
RBRAKET: ']';
DOUBLE_QUOTE: '"';
EOL: '\n';
CONST: 'const';
TRUE: 'true';
FALSE: 'false';

// Parameter types
OPTION: 'opt';
ARGUMENT: 'arg';

// Props
ACTION_PROP: 'action';
ALIASES_PROP: 'aliases';
CHILDREN_PROP: 'children';

// Comments
MULTILINE_COMMENT_START: '/*';
MULTILINE_COMMENT_END: '*/';
COMMENT: '//';

INT : [0-9]+ ;
NAME: [a-zA-Z_][a-zA-Z_0-9]* ;
WS: [ \t\n\r\f]+ -> skip ;
ANYTHING: .*?;
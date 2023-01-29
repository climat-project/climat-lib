// DELETE THIS CONTENT IF YOU PUT COMBINED GRAMMAR IN Parser TAB
lexer grammar CliDslLexer;

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
QMARK: '?';
EOL: '\n';
CONST: 'const';
TRUE: 'true';
FALSE: 'false';

// Parameters
FLAG: 'flag';
ARGUMENT: 'arg';
SHORTHAND: [a-zA-Z];

// Props
ACTION_PROP: 'action';
ALIASES_PROP: 'aliases';
CHILDREN_PROP: 'children';

// Default
OVERRIDE: 'override';
DEFAULT: 'default';

// Comments
MULTILINE_COMMENT_START: '/*';
MULTILINE_COMMENT_END: '*/';
COMMENT: '//';

INT : [0-9]+ ;
NAME: [a-zA-Z_\-][a-zA-Z_0-9\-]* ;
WS: [ \t\n\r\f]+ -> skip;
STRING_LITERAL: '"' ( '\\"' | . )*? '"';
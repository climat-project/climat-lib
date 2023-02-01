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
ALPHANUMERIC: [a-zA-Z0-9];

// Parameters
FLAG: 'flag';
ARGUMENT: 'arg';

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

// Modifiers
SEALED: 'sealed';
SHIFTED: 'shifted';

// Actions
SCOPE_PARAMS: 'scope params';

INT : [0-9]+;
IDENTIFIER: (ALPHANUMERIC | [_-])+;
WS: [ \t\n\r\f]+ -> skip;
STRING_LITERAL: '"' ( '\\"' | . )*? '"';

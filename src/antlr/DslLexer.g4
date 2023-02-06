// DELETE THIS CONTENT IF YOU PUT COMBINED GRAMMAR IN Parser TAB
lexer grammar DslLexer;
import CommonLexer;

EQ : '=' ;
COMMA : ',' ;
COLON : ':' ;
LPAREN : '(' ;
RPAREN : ')' ;
LCURLY : '{' ;
RCURLY : '}' ;
LBRAKET: '[';
RBRAKET: ']';
LT: '<';
GT: '>';
DOUBLE_QUOTE: '"';
QMARK: '?';
CONST: 'const';
TRUE: 'true';
FALSE: 'false';

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
CUSTOM_SCRIPT: LT WS* .*? WS* GT;

WS: [ \t\n\r\f]+ -> skip;
STRING_LITERAL: '"' ( '\\"' | . )*? '"';

// Docstring
DOCSTRING: '"""' .*? '"""';

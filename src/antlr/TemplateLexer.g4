lexer grammar TemplateLexer;

import CommonLexer;

KACHING: '$';
LPAREN : '(' ;
RPAREN : ')' ;
COLON: ':';
NEGATE: '!';
CONTENT: ( '\\$' | . )+?;
WS: [ \t\n\r\f]+;

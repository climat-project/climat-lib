lexer grammar DocstringLexer;

import CommonLexer;

WS: [ \t\n\r\f]+;
AT_PARAM: '@param';
CONTENT: ~[@" \t\n\r\f]+?;
// DELETE THIS CONTENT IF YOU PUT COMBINED GRAMMAR IN Parser TAB
lexer grammar DslLexer;

channels {
  WHITESPACE_CHANNEL,
  COMMENT
}

EQ : '=' ;
COMMA : ',' ;
COLON : ':' ;
LPAREN : '(' ;
RPAREN : ')' ;
LCURLY : '{' ;
RCURLY : '}' ;
LBRAKET: '[';
RBRAKET: ']';
DOUBLE_QUOTE: '"' -> pushMode(Template);
QMARK: '?';
CONST: 'const';
TRUE: 'true';
FALSE: 'false';

// Parameters
FLAG: 'flag';
ARGUMENT: 'arg';

// Props
ACTION_PROP: 'action';

// Default
OVERRIDE: 'override';
DEFAULT: 'default';

// Comments
MULTILINE_COMMENT: '/*' .*? '*/' -> channel(COMMENT);
SINGLELINE_COMMENT: '//' .*? '\n' -> channel(COMMENT);

// Sub
MOD_SEAL: '@seal';
MOD_SHIFT: '@shift';
MOD_ALIAS: '@alias';
MOD_ALIASES: '@aliases';
MOD_ALLOW_UNMATCHED: '@allow-unmatched';

SUB: 'sub';

// Actions
SCOPE_PARAMS: 'scope params';
CUSTOM_SCRIPT_BEGIN: '<' -> pushMode(CustomScript);

// Docstring
DOCSTRING_BEGIN: '"""' -> pushMode(Docstring);

WS: [ \t\n\r\f]+ -> channel(WHITESPACE_CHANNEL);
fragment ALPHANUMERIC: [a-zA-Z0-9];
IDENTIFIER: (ALPHANUMERIC | [_-])+;

mode Template;
Template_CONTENT: ('\\$' | '\\"' | ~[$"])+;
Template_INTERPOLATION_OPEN: '$' LPAREN -> pushMode(Interpolation);
Template_CLOSE: '"' -> popMode;

mode Interpolation;
Interpolation_IDENTIFIER: IDENTIFIER;
Interpolation_COLON: COLON;
Interpolation_RPAREN: RPAREN -> popMode;
Interpolation_WS: WS;
Interpolation_NEGATE: '!';

mode Docstring;
Docstring_AT_PARAM: '@param' WS -> pushMode(DocstringRef);
Docstring_CONTENT: ~[@"]+;
Docstring_END: '"""' -> popMode;

mode DocstringRef;
Docstring_IDENTIFIER: IDENTIFIER WS -> popMode;

mode CustomScript;
CustomScript_SCRIPT: (~[>] | '>>')+;
CustomScript_END: '>' -> popMode;

parser grammar TemplateParser;
options { tokenVocab=TemplateLexer; }

root: entry* EOF;
entry: content | interpolation;
content: CONTENT | IDENTIFIER;
interpolation: KACHING LPAREN WS* NEGATE? WS* IDENTIFIER mapping? WS* RPAREN;
mapping: WS* COLON WS* IDENTIFIER WS*;
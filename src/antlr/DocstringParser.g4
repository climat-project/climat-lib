parser grammar DocstringParser;
options { tokenVocab=DocstringLexer; }

root: entry* EOF;
entry: CONTENT | paramTag;
paramTag: AT_PARAM IDENTIFIER WS+;
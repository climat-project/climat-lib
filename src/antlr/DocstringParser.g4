parser grammar DocstringParser;
options { tokenVocab=DocstringLexer; }

root: entries*;
entries: CONTENT | paramTag;
paramTag: AT_PARAM IDENTIFIER WS+;
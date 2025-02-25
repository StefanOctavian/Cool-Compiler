lexer grammar CoolLexer;

tokens {
	ERROR
}

@header {
package cool.lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;	
}

@members {    
    private void raiseError(String msg) {
        setText(msg);
        setType(ERROR);
    }

    private String replaceEscapes(String str) {
        return str.replaceAll("\\\\\"", "\"").replaceAll("\\\\n", "\n")
                  .replaceAll("\\\\b", "\b").replaceAll("\\\\t", "\t")
                  .replaceAll("\\\\f", "\f").replaceAll("\\\\(\\r?)\\n", "$1\n")
                  .replaceAll("\\\\(.)", "$1");
    }

    private void rewriteString() {
        if (getType() == ERROR) return;
        String str = getText();
        str = str.substring(1, str.length() - 1);
        StringBuilder builder = new StringBuilder();
        Pattern pattern = Pattern.compile("\\\\\\\\");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String substr = replaceEscapes(str.substring(0, matcher.start()));
            str = str.substring(matcher.end());
            builder.append(substr + "\\");
            matcher = pattern.matcher(str);
        }
        builder.append(replaceEscapes(str));
        if (builder.toString().length() > 1024) raiseError("String constant too long");
        else setText(builder.toString());
    }
}

// whitespace 
WS: [ \n\f\r\t\u000B]+ -> skip;

// case insensitive letters
fragment A: [aA];
// fragment B: [bB];
fragment C: [cC];
fragment D: [dD];
fragment E: [eE];
fragment F: [fF];
fragment G: [gG];
fragment H: [hH];
fragment I: [iI];
fragment J: [jJ];
fragment K: [kK];
fragment L: [lL];
fragment M: [mM];
fragment N: [nN];
fragment O: [oO];
fragment P: [pP];
fragment Q: [qQ];
fragment R: [rR];
fragment S: [sS];
fragment T: [tT];
fragment U: [uU];
fragment V: [vV];
fragment W: [wW];
fragment X: [xX];
fragment Y: [yY];
fragment Z: [zZ];

// numbers
fragment DIGIT: [0-9];
INTEGER: DIGIT+;

// keywords (in alphabetical order)
CASE: C A S E;
CLASS: C L A S S;
ELSE: E L S E;
ESAC: E S A C;
FALSE: 'f' A L S E;
FI: F I;
IF: I F;
IN: I N;
INHERITS: I N H E R I T S;
ISVOID: I S V O I D;
LET: L E T;
LOOP: L O O P;
NEW: N E W;
NOT: N O T;
OF: O F;
POOL: P O O L;
THEN: T H E N;
TRUE: 't' R U E;
WHILE: W H I L E;

// identifiers
fragment UPPERCASE: [A-Z];
fragment LOWERCASE: [a-z];
fragment LETTER: UPPERCASE | LOWERCASE;
ID: LOWERCASE (LETTER | DIGIT | '_')*;
TYPE: UPPERCASE (LETTER | DIGIT | '_')*;

// symbols
LPAREN: '(';
RPAREN: ')';
DOT: '.';
EQ: '=';
LT: '<';
LEQ: '<=';
NEG: '~';
DIV: '/';
MUL: '*';
SUB: '-';
PLUS: '+';
DARROW: '=>';
ASSIGN: '<-';
COLON: ':';
SEMICOLON: ';';
COMMA: ',';
AT: '@';
LBRACE: '{';
RBRACE: '}';

// strings
fragment NEWLINE: '\n' | '\r\n';
fragment NULCHAR: '\u0000';
STRING:
	'"' ('\\' NEWLINE | '\\"' | NULCHAR { raiseError("String contains null character"); } | .)*? (
		'"'
		| NEWLINE { raiseError("Unterminated string constant"); }
		| EOF { raiseError("EOF in string constant"); }
	) { rewriteString(); };

// comments
LINE_COMM: '--' .*? NEWLINE -> skip;
BLOCK_COMM:
	'(*' (BLOCK_COMM | .)*? (
		'*)' { skip(); }
		| EOF { raiseError("EOF in comment"); }
	);
UNMATCHED_COMM_END: '*)' { raiseError("Unmatched *)"); };

// invalid character
INVALID_CHAR: . { raiseError("Invalid character: " + getText()); };
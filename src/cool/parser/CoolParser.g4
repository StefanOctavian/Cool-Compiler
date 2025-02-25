parser grammar CoolParser;

options {
	tokenVocab = CoolLexer;
}

@header {
    package cool.parser;
}

program: (classes += class SEMICOLON)+;

class:
	CLASS name = TYPE (INHERITS base = TYPE)? LBRACE (
		(methods += method | attributes += attribute) SEMICOLON
	)* RBRACE;

vardef: ID COLON TYPE (ASSIGN expr)?;

method:
	ID LPAREN (formal (COMMA formal)*)? RPAREN COLON TYPE LBRACE expr RBRACE;
attribute: vardef;

formal: ID COLON TYPE;

call: ID LPAREN (expr (COMMA expr)*)? RPAREN;
caseBranch: ID COLON TYPE DARROW expr SEMICOLON;

var: ID;

expr:
	op = NEW TYPE														# newExpr
	| expr (AT TYPE)? DOT call											# explDispatch
	| call																# implDispatch
	| IF cond = expr THEN thenBranch = expr ELSE elseBranch = expr FI	# if
	| WHILE cond = expr LOOP body = expr POOL							# loop
	| LBRACE (expr SEMICOLON)+ RBRACE									# block
	| LET vardef (COMMA vardef)* IN expr								# let
	| CASE expr OF caseBranch+ ESAC										# case
	| op = NEG expr														# unaryOp
	| op = ISVOID expr													# unaryOp
	| expr op = (MUL | DIV) expr										# binaryOp
	| expr op = (PLUS | SUB) expr										# binaryOp
	| expr op = (LT | LEQ | EQ) expr									# binaryOp
	| op = NOT expr														# unaryOp
	| var ASSIGN expr													# assigment
	| LPAREN expr RPAREN												# precedence
	| var																# exprvar
	| INTEGER															# literal
	| STRING															# literal
	| TRUE																# literal
	| FALSE																# literal;
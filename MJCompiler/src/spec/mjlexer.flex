package rs.ac.bg.etf.pp1;
import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT
%xstate CHARLITERAL

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"program"   { return new_symbol(sym.PROGRAM, yytext()); }
"break"     { return new_symbol(sym.BREAK, yytext()); }
"class"     { return new_symbol(sym.CLASS, yytext()); }
"enum"      { return new_symbol(sym.ENUM, yytext()); }
"else"      { return new_symbol(sym.ELSE, yytext()); }
"const"     { return new_symbol(sym.CONST, yytext()); }
"if"        { return new_symbol(sym.IF, yytext()); }
"switch"    { return new_symbol(sym.SWITCH, yytext()); }
"do"        { return new_symbol(sym.DO, yytext()); }
"while"     { return new_symbol(sym.WHILE, yytext()); }
"new"       { return new_symbol(sym.NEW, yytext()); }
"print" 	{ return new_symbol(sym.PRINT, yytext()); }
"read"      { return new_symbol(sym.READ, yytext());}
"return" 	{ return new_symbol(sym.RETURN, yytext()); }
"void" 		{ return new_symbol(sym.VOID, yytext()); }
"extends"   { return new_symbol(sym.EXTENDS, yytext()); }
"continue"  { return new_symbol(sym.CONTINUE, yytext()); }
"case"      { return new_symbol(sym.CASE, yytext()); }
"default"   { return new_symbol(sym.DEFAULT, yytext()); }
"++"        { return new_symbol(sym.POSTINC, yytext()); }
"--"        { return new_symbol(sym.POSTDEC, yytext()); }
"+" 		{ return new_symbol(sym.ADD, yytext()); }
"-"         { return new_symbol(sym.SUB, yytext()); }
"*"         { return new_symbol(sym.MUL, yytext()); }
"/"         { return new_symbol(sym.DIV, yytext()); }
"%"         { return new_symbol(sym.MOD, yytext()); }
"==" 		{ return new_symbol(sym.EQUALS, yytext()); }
"!="        { return new_symbol(sym.NOT_EQUALS, yytext()); }
">="        { return new_symbol(sym.GREATER_THAN_OR_EQUAL_TO, yytext()); }
">"         { return new_symbol(sym.GREATER_THAN, yytext()); }
"<="        { return new_symbol(sym.LESSER_THAN_OR_EQUAL_TO, yytext()); }
"<"         { return new_symbol(sym.LESSER_THAN, yytext()); }
"&&"        { return new_symbol(sym.LOGICAL_AND, yytext()); }
"||"        { return new_symbol(sym.LOGICAL_OR, yytext()); }
"="         { return new_symbol(sym.ASSIGN, yytext()); }
";" 		{ return new_symbol(sym.SEMICOLON, yytext()); }
"," 		{ return new_symbol(sym.COMMA, yytext()); }
"."         { return new_symbol(sym.DOT, yytext()); }
"(" 		{ return new_symbol(sym.LEFT_PARENTHESIS, yytext()); }
")" 		{ return new_symbol(sym.RIGHT_PARENTHESIS, yytext()); }
"["         { return new_symbol(sym.LEFT_SQUARE_BRACKET, yytext()); }
"]"         { return new_symbol(sym.RIGHT_SQUARE_BRACKET, yytext()); }
"{" 		{ return new_symbol(sym.LEFT_CURLY_BRACE, yytext()); }
"}"			{ return new_symbol(sym.RIGHT_CURLY_BRACE, yytext()); }
"?"         { return new_symbol(sym.QUESTION_MARK, yytext()); }
":"         { return new_symbol(sym.COLON, yytext()); }

"//" {yybegin(COMMENT);}
<COMMENT> . {yybegin(COMMENT);}
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

true|false { return new_symbol(sym.BOOLCONST, new Boolean(yytext())); }

\'[\x21-\x7E]\' { return new_symbol (sym.CHARCONST, new Character(yytext().charAt(1))); }

[0-9]+ { return new_symbol(sym.NUMCONST, new Integer (yytext())); }
[a-zA-Z][a-zA-Z0-9_]* {return new_symbol (sym.IDENT, yytext()); }


. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1) + " u koloni " + (yycolumn+1)); }

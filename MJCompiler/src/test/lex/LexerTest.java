package test.lex;

import org.junit.*;
import java_cup.runtime.*;
import rs.ac.bg.etf.pp1.sym;
import rs.ac.bg.etf.pp1.MJCompiler;

public class LexerTest {
    private Scanner lexer1;
    private Scanner lexer2;
    private Scanner lexer3;
    private Scanner lexer4;

    @Before
    public void init() {
        lexer1 = MJCompiler.getNewLexer("bin/testKeywords.mj");
        lexer2 = MJCompiler.getNewLexer("bin/testConstants.mj");
        lexer3 = MJCompiler.getNewLexer("bin/testIdents.mj");
        lexer4 = MJCompiler.getNewLexer("bin/testOperators.mj");
    }

    @Test
    public void testKeywords() throws java.lang.Exception {
        Assert.assertEquals(lexer1.next_token().sym, sym.PROGRAM);
        Assert.assertEquals(lexer1.next_token().sym, sym.BREAK);
        Assert.assertEquals(lexer1.next_token().sym, sym.CLASS);
        Assert.assertEquals(lexer1.next_token().sym, sym.ENUM);
        Assert.assertEquals(lexer1.next_token().sym, sym.ELSE);
        Assert.assertEquals(lexer1.next_token().sym, sym.CONST);
        Assert.assertEquals(lexer1.next_token().sym, sym.IF);
        Assert.assertEquals(lexer1.next_token().sym, sym.SWITCH);
        Assert.assertEquals(lexer1.next_token().sym, sym.DO);
        Assert.assertEquals(lexer1.next_token().sym, sym.WHILE);
        Assert.assertEquals(lexer1.next_token().sym, sym.IDENT);
        Assert.assertEquals(lexer1.next_token().sym, sym.IDENT);
        Assert.assertEquals(lexer1.next_token().sym, sym.IDENT);
        Assert.assertEquals(lexer1.next_token().sym, sym.NEW);
        Assert.assertEquals(lexer1.next_token().sym, sym.PRINT);
        Assert.assertEquals(lexer1.next_token().sym, sym.READ);
        Assert.assertEquals(lexer1.next_token().sym, sym.RETURN);
        Assert.assertEquals(lexer1.next_token().sym, sym.VOID);
        Assert.assertEquals(lexer1.next_token().sym, sym.IDENT);
        Assert.assertEquals(lexer1.next_token().sym, sym.EXTENDS);
        Assert.assertEquals(lexer1.next_token().sym, sym.CONTINUE);
        Assert.assertEquals(lexer1.next_token().sym, sym.CASE);
        Assert.assertEquals(lexer1.next_token().sym, sym.IDENT);
        Assert.assertEquals(lexer1.next_token().sym, sym.IDENT);
        Assert.assertEquals(lexer1.next_token().sym, sym.IDENT);
    }

    @Test
    public void testConstants() throws java.lang.Exception {
        Assert.assertEquals(lexer2.next_token().sym, sym.NUMCONST);
        Assert.assertEquals(lexer2.next_token().sym, sym.NUMCONST);
        Assert.assertEquals(lexer2.next_token().sym, sym.BOOLCONST);
        Assert.assertEquals(lexer2.next_token().sym, sym.IDENT);
        Assert.assertEquals(lexer2.next_token().sym, sym.IDENT);
        Assert.assertEquals(lexer2.next_token().sym, sym.BOOLCONST);
        Assert.assertEquals(lexer2.next_token().sym, sym.CHARCONST);

    }

    @Test
    public void testIdents() throws java.lang.Exception {
        Assert.assertEquals(lexer3.next_token().sym, sym.IDENT);
        Assert.assertEquals(lexer3.next_token().sym, sym.IDENT);
        Assert.assertEquals(lexer3.next_token().sym, sym.NUMCONST);
        Assert.assertEquals(lexer3.next_token().sym, sym.IDENT);
    }

    @Test
    public void testOperators() throws java.lang.Exception {
        Assert.assertEquals(lexer4.next_token().sym, sym.ADD);
        Assert.assertEquals(lexer4.next_token().sym, sym.SUB);
        Assert.assertEquals(lexer4.next_token().sym, sym.MUL);
        Assert.assertEquals(lexer4.next_token().sym, sym.DIV);
        Assert.assertEquals(lexer4.next_token().sym, sym.MOD);
        Assert.assertEquals(lexer4.next_token().sym, sym.EQUALS);
        Assert.assertEquals(lexer4.next_token().sym, sym.GREATER_THAN);
        Assert.assertEquals(lexer4.next_token().sym, sym.GREATER_THAN_OR_EQUAL_TO);
        Assert.assertEquals(lexer4.next_token().sym, sym.ASSIGN);
        Assert.assertEquals(lexer4.next_token().sym, sym.LESSER_THAN);
        Assert.assertEquals(lexer4.next_token().sym, sym.LESSER_THAN_OR_EQUAL_TO);
        Assert.assertEquals(lexer4.next_token().sym, sym.NOT_EQUALS);
        Assert.assertEquals(lexer4.next_token().sym, sym.LOGICAL_AND);
        Assert.assertEquals(lexer4.next_token().sym, sym.LOGICAL_OR);
        Assert.assertEquals(lexer4.next_token().sym, sym.ASSIGN);
        Assert.assertEquals(lexer4.next_token().sym, sym.COLON);
        Assert.assertEquals(lexer4.next_token().sym, sym.POSTINC);
        Assert.assertEquals(lexer4.next_token().sym, sym.POSTDEC);
        Assert.assertEquals(lexer4.next_token().sym, sym.LESSER_THAN);
        Assert.assertEquals(lexer4.next_token().sym, sym.SEMICOLON);
        Assert.assertEquals(lexer4.next_token().sym, sym.DOT);
        Assert.assertEquals(lexer4.next_token().sym, sym.COMMA);
        Assert.assertEquals(lexer4.next_token().sym, sym.LEFT_PARENTHESIS);
        Assert.assertEquals(lexer4.next_token().sym, sym.LEFT_CURLY_BRACE);
        Assert.assertEquals(lexer4.next_token().sym, sym.RIGHT_SQUARE_BRACKET);
        Assert.assertEquals(lexer4.next_token().sym, sym.LEFT_SQUARE_BRACKET);
        Assert.assertEquals(lexer4.next_token().sym, sym.RIGHT_CURLY_BRACE);
        Assert.assertEquals(lexer4.next_token().sym, sym.RIGHT_PARENTHESIS);
        Assert.assertEquals(lexer4.next_token().sym, sym.QUESTION_MARK);
    }
}

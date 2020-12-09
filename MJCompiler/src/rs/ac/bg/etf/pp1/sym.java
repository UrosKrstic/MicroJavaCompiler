package rs.ac.bg.etf.pp1;

/**
 * sym
 */
public class sym {
    // Keywords
	public static final int PROGRAM = 1;
	public static final int BREAK = 2;
    public static final int CLASS = 3;
    public static final int ENUM = 4;
    public static final int ELSE = 5;
    public static final int CONST = 6;
    public static final int IF = 7;
    public static final int SWITCH = 8;
    public static final int DO = 9;
    public static final int WHILE = 10;
    public static final int NEW = 11;
    public static final int PRINT = 12;
    public static final int READ = 13;
    public static final int RETURN = 14;
    public static final int VOID = 15;
    public static final int EXTENDS = 16;
    public static final int CONTINUE = 17;
    public static final int CASE = 18;
	
	// Identifiers
	public static final int IDENT = 19;
	
	// Constants
    public static final int NUMCONST = 20;
    public static final int CHARCONST = 21;
    public static final int BOOLCONST = 22;
	
    // Operators:
    //  - Arithemic
    public static final int ADD = 23;
    public static final int SUB = 24;
	public static final int MUL = 25;
	public static final int DIV = 26;
    public static final int MOD = 27;
    //  - Relational
	public static final int EQUALS = 28;
	public static final int NOT_EQUALS = 29;
	public static final int GREATER_THAN = 30;
    public static final int GREATER_THAN_OR_EQUAL_TO = 32;
    public static final int LESSER_THAN = 33;
    public static final int LESSER_THAN_OR_EQUAL_TO = 34;
    //  - Logical
    public static final int LOGICAL_AND = 35;
    public static final int LOGICAL_OR = 36;
    //  - Assignment
    public static final int ASSIGN = 37;
    //  - Post Increment/Decrement
    public static final int POSTINC = 38;
    public static final int POSTDEC = 39;
    //  - Semicolon
    public static final int SEMICOLON = 40;
    //  - COMMA
    public static final int COMMA = 41;
    // - DOT (MEMBER OPERATOR)
    public static final int DOT = 42;
    //  - BRACKETS
    public static final int LEFT_PARENTHESIS = 43;
    public static final int RIGHT_PARENTHESIS = 44;
    public static final int LEFT_SQUARE_BRACKET = 45;
    public static final int RIGHT_SQUARE_BRACKET = 46;
    public static final int LEFT_CURLY_BRACE = 47;
    public static final int RIGHT_CURLY_BRACE = 48;
    //  - QUESTION MARK
    public static final int QUESTION_MARK = 49;
    //  - COLON
    public static final int COLON = 50;

    // EOF
	public static final int EOF = -1;
}
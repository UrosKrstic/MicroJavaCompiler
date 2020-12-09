package rs.ac.bg.etf.pp1;

import java.io.File;
import java.io.FileReader;

import java_cup.runtime.*;

public class MJCompiler {
    public static void main(String[] args) {  
        if (args.length < 1) {
            System.out.println("Error: Missing MJ source code file command-line arg");
            return;
        }
        try {
            File file = new File(args[0]);
            FileReader reader = new FileReader(file);
            Scanner lexer = new Yylex(reader);
            Symbol token = lexer.next_token();
            while (token.sym != sym.EOF) {
                System.out.println("[" + token.sym + "]" + token.value.toString());
                token = lexer.next_token();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Scanner getNewLexer(String filename) {
        try {
            File file = new File(filename);
            FileReader reader = new FileReader(file);
            return new Yylex(reader);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

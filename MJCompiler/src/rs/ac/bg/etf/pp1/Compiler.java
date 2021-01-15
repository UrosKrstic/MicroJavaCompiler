package rs.ac.bg.etf.pp1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import java_cup.runtime.*;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.etf.pp1.mj.runtime.Code;

public class Compiler {

    static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}

    public static void main(String[] args) {  
        Logger logger = Logger.getLogger(Compiler.class);
        if (args.length < 1) {
            logger.error("Missing MJ source code file command-line arg");
            return;
        }
        if (args.length < 2) {
            logger.error("Missing output file name command-line arg");
            return;
        }
        FileReader reader = null;
        try {
            File file = new File(args[0]);
            logger.info("Compiling source file: " + args[0]);
            reader = new FileReader(file);
            Scanner lexer = new Yylex(reader);

            MJParser parser = new MJParser(lexer);
            Symbol s = parser.parse();

            Program program = null;
            if (s.value instanceof Program)
                program = (Program) (s.value);
            else {
                logger.error("Syntax Error exiting program");
                return;
            }
            logger.info(program.toString(""));

            logger.info("==========================SEMANTIC ANALYSIS==============================");
            MySymbolTable.init();
            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
            program.traverseBottomUp(semanticAnalyzer);

            MySymbolTable.dump(new MyDumpSymbolTableVisitor());

            if (semanticAnalyzer.passed()) {
                logger.info("Parsing has been successful");
                logger.info("==========================CODE GENERATION==============================");
                CodeGenerator codeGenerator = new CodeGenerator();
                program.traverseBottomUp(codeGenerator);
                Code.dataSize = semanticAnalyzer.globalDataCount;
                Code.mainPc = codeGenerator.mainPc;
                File objFile = new File(args[1]);
                if (objFile.exists()) objFile.delete();
                Code.write(new FileOutputStream(objFile));
            }
            else {
                logger.error("Parsing has been unsuccessful");
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    public static Scanner getNewLexer(String filename) throws Exception {
        Logger logger = Logger.getLogger(Compiler.class);
        FileReader reader = null;
        try {
            File file = new File(filename);
            reader = new FileReader(file);
            return new Yylex(reader);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }
}

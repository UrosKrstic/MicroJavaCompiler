package rs.ac.bg.etf.pp1;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import rs.ac.bg.etf.pp1.util.Log4JUtils;

import java_cup.runtime.*;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class MJCompiler {

    static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}

    public static void main(String[] args) {  
        if (args.length < 1) {
            System.out.println("Error: Missing MJ source code file command-line arg");
            return;
        }
        Logger logger = Logger.getLogger(MJCompiler.class);
        FileReader reader = null;
        try {
            File file = new File(args[0]);
            logger.info("Compiling source file: " + args[0]);
            reader = new FileReader(file);
            Scanner lexer = new Yylex(reader);
            Symbol token = lexer.next_token();
            while (token.sym != sym.EOF) {
                logger.info("[" + token.sym + "] " + token.value.toString());
                token = lexer.next_token();
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
        Logger logger = Logger.getLogger(MJCompiler.class);
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

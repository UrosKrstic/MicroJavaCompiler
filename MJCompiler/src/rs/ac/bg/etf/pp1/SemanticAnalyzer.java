package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;


public class SemanticAnalyzer extends VisitorAdaptor {

    boolean errorDetected = false;

    Logger log = Logger.getLogger(getClass());

    public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

    public boolean passed(){
    	return !errorDetected;
    }
}

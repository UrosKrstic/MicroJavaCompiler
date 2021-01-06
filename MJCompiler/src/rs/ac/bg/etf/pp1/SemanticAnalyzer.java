package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.LinkedHashMap;
import java.util.Map;

public class SemanticAnalyzer extends VisitorAdaptor {

    private static boolean errorDetected = false;
    private static Struct currentType = MySymbolTable.noType;
    public static Map<Struct, Struct> tableOfArrayStructs = new LinkedHashMap<Struct, Struct>();

    Logger log = Logger.getLogger(getClass());

    public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (", line ").append(line);
		log.error(msg.toString());
    }

    public void report_info(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (", line ").append(line);
		log.info(msg.toString());
    }

    public boolean passed(){
    	return !errorDetected;
    }

    public void visit(ProgName progName) {
        progName.obj = MySymbolTable.insert(Obj.Prog, progName.getProgName(), MySymbolTable.noType);
        MySymbolTable.openScope();
    }

    public void visit(Program program) {
        MySymbolTable.chainLocalSymbols(program.getProgName().obj);
        MySymbolTable.closeScope();
    }

    public void visit(VarDecl varDecl) {
        // check if already declared //
        Obj varNode = MySymbolTable.find(varDecl.getVarName());
        if (varNode != MySymbolTable.noObj) {
            report_error("Variable name '" + varDecl.getVarName() + "' already declared", varDecl);
            return;
        }

        boolean isArray = varDecl.getOptionalArrayBrackets() instanceof ArrayBrackets;
        if (isArray) {
            Struct arrStruct = tableOfArrayStructs.get(currentType);
            if (arrStruct == null) {
                arrStruct = new Struct(Struct.Array, currentType);
                tableOfArrayStructs.put(currentType, arrStruct);
            }
            Obj objNode = MySymbolTable.insert(Obj.Var, varDecl.getVarName(), arrStruct);
            MyDumpSymbolTableVisitor visitor = new MyDumpSymbolTableVisitor();
            objNode.accept(visitor);
            report_info("Declared array variable '" + varDecl.getVarName() + "', symbol: " 
            + "[" + visitor.getOutput() + "]", varDecl);
        }
        else {
            Obj objNode = MySymbolTable.insert(Obj.Var, varDecl.getVarName(), currentType);
            MyDumpSymbolTableVisitor visitor = new MyDumpSymbolTableVisitor();
            objNode.accept(visitor);
            report_info("Declared variable '" + varDecl.getVarName() + "', symbol: " 
            + "[" + visitor.getOutput() + "]", varDecl);
        }
    }

    public void visit(Type type) {
        Obj typeNode = MySymbolTable.find(type.getTypeName());
        if (typeNode == MySymbolTable.noObj) {
            report_error("Type '" + type.getTypeName() + "' not found in symbol table", type);
            currentType = MySymbolTable.noType;
        }
        else {
            if (Obj.Type == typeNode.getKind()) {
                currentType = typeNode.getType();
            }
            else {
                report_error("Name '" + type.getTypeName() + "' is not a type" , type);
                currentType = MySymbolTable.noType;
            }
        }
    }
}

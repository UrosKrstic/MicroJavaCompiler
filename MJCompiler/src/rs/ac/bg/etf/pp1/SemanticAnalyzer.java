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
    private static String currentTypeName = "noType";
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

    public String stringifyObjNode(Obj objNode) {
        MyDumpSymbolTableVisitor visitor = new MyDumpSymbolTableVisitor();
        objNode.accept(visitor);
        return visitor.getOutput();
    }

    public boolean checkForMultipleDeclarations(String symbolName, SyntaxNode info, String messageStart) {
        Obj objNode = MySymbolTable.find(symbolName);
        if (objNode != MySymbolTable.noObj) {
            report_error(messageStart + " '" + symbolName + "' already declared", info);
            return true;
        }
        return false;
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
        if (checkForMultipleDeclarations(varDecl.getVarName(), varDecl, "Variable name")) return;

        boolean isArray = varDecl.getOptionalArrayBrackets() instanceof ArrayBrackets;
        if (isArray) { // is array
            Struct arrStruct = tableOfArrayStructs.get(currentType);
            if (arrStruct == null) {
                arrStruct = new Struct(Struct.Array, currentType);
                tableOfArrayStructs.put(currentType, arrStruct);
            }
            Obj objNode = MySymbolTable.insert(Obj.Var, varDecl.getVarName(), arrStruct);
            report_info("Declared array variable '" + varDecl.getVarName() + "', symbol: " 
            + "[" + stringifyObjNode(objNode) + "]", varDecl);
        }
        else { // is regular variable
            Obj objNode = MySymbolTable.insert(Obj.Var, varDecl.getVarName(), currentType);
            report_info("Declared variable '" + varDecl.getVarName() + "', symbol: " 
            + "[" + stringifyObjNode(objNode) + "]", varDecl);
        }
    }

    public void visit(ConstDecl constDecl) {
        if (checkForMultipleDeclarations(constDecl.getConstName(), constDecl, "Constant name")) return;

        if (currentType.getKind() == Struct.Bool ||
            currentType.getKind() == Struct.Int ||
            currentType.getKind() == Struct.Char) {

            int constType = Struct.Int;
            if (constDecl.getConstValue() instanceof CharConst) {
                constType = Struct.Char;
            }
            else if (constDecl.getConstValue() instanceof BooleanConst) {
                constType = Struct.Bool;
            }

            if (constType == currentType.getKind()) {
                Obj objNode = MySymbolTable.insert(Obj.Con, constDecl.getConstName(), currentType);
                report_info("Declared constant '" + constDecl.getConstName() + "', symbol: " 
                + "[" + stringifyObjNode(objNode) + "]", constDecl);
            }
            else {
                report_error("Invalid value for given type '" + currentTypeName + "' in constant declaration ", constDecl);
            }
        }
        else {
            report_error("Invalid type '" + currentTypeName + "' used in constant declaration", constDecl);
        }
    }

    public void visit(Type type) {
        Obj typeNode = MySymbolTable.find(type.getTypeName());
        currentTypeName = type.getTypeName();
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

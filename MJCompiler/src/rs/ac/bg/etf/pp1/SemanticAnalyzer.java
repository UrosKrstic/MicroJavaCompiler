package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class SemanticAnalyzer extends VisitorAdaptor {
    private static boolean errorDetected = false;

    // Declarations
    private static Struct currentType = MySymbolTable.noType;
    private static String currentTypeName = "noType";
    public static Map<Struct, Struct> tableOfArrayStructs = new LinkedHashMap<Struct, Struct>();

    // Methods
    private static Obj currentMethod = null;
    private static boolean isClassMethod = false;
    private static boolean isFormParamDecl = false;
    private static Map<String, Obj> currentMethodFormParams = new LinkedHashMap<String, Obj>();

    // Exprs
    private static boolean accessArray = false;
    private static Obj currentDesignatorObj = null;
    private static Queue<Obj> innerDesignators = new LinkedList<>();

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

    public boolean checkForMultipleDeclarations(String symbolName, int level, SyntaxNode info, String messageStart) {
        Obj objNode = MySymbolTable.find(symbolName);
        if (objNode != MySymbolTable.noObj && objNode.getLevel() == level) {
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
        int level = currentMethod == null ? 0 : 1;

        if (checkForMultipleDeclarations(varDecl.getVarName(), level, varDecl, "Variable name")) 
            return;

        boolean isArray = varDecl.getOptionalArrayBrackets() instanceof ArrayBrackets;
        Obj objNode = null;
        if (isArray) { // is array
            Struct arrStruct = tableOfArrayStructs.get(currentType);
            if (arrStruct == null) {
                arrStruct = new Struct(Struct.Array, currentType);
                tableOfArrayStructs.put(currentType, arrStruct);
            }
            objNode = MySymbolTable.insert(Obj.Var, varDecl.getVarName(), arrStruct);
            report_info("Declared array variable '" + varDecl.getVarName() + "', symbol: " 
            + "[" + stringifyObjNode(objNode) + "]", varDecl);
        }
        else { // is regular variable
            objNode = MySymbolTable.insert(Obj.Var, varDecl.getVarName(), currentType);
            report_info("Declared variable '" + varDecl.getVarName() + "', symbol: " 
            + "[" + stringifyObjNode(objNode) + "]", varDecl);
        }
        if (isFormParamDecl && objNode != null) {
            currentMethodFormParams.put(objNode.getName(), objNode);
        }
    }

    public void visit(ConstDecl constDecl) {
        if (checkForMultipleDeclarations(constDecl.getConstName(), 0, constDecl, "Constant name"))
            return;

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
                report_error("Invalid value for given type '" + currentTypeName +
                    "' in constant declaration ", constDecl);
            }
        }
        else {
            report_error("Invalid type '" + currentTypeName + "' used in constant declaration", constDecl);
        }
    }

    public void visit(MethodReturnTypeAndName methodReturnTypeAndName) {
        Struct returnType = MySymbolTable.noType;
        if (methodReturnTypeAndName.getVoidOrType() instanceof ReturnType) {
            returnType = currentType;
        }
        currentMethod = MySymbolTable.insert(Obj.Meth, methodReturnTypeAndName.getMethodName(), returnType);
        methodReturnTypeAndName.obj = currentMethod;
        MySymbolTable.openScope();
        report_info("Function definition started: " +  methodReturnTypeAndName.getMethodName(), 
            methodReturnTypeAndName);
    }

    public void visit(MethodDecl methodDecl) {
        MySymbolTable.chainLocalSymbols(currentMethod);
        MySymbolTable.closeScope();
        currentMethod.setLevel(currentMethodFormParams.size());
        report_info("Function definition ended: " + stringifyObjNode(currentMethod), methodDecl);
        currentMethod = null;
        currentMethodFormParams.clear();
    }

    public void visit(FormArgsStart formArgsStart) {
        isFormParamDecl = true;
    }

    public void visit(FormArgsEnd formArgsEnd) {
        isFormParamDecl = false;
    }

    public void visit(SingleDesignator singleDesignator) {
        currentDesignatorObj = MySymbolTable.find(singleDesignator.getName());
        if (currentDesignatorObj == MySymbolTable.noObj) {
            report_error("Undeclared symbol '" + singleDesignator.getName() + "'", singleDesignator);
        }
    }

    public void visit(InnerExprInBracketsDesignator exprInBracketsDesignator) {
        if (currentDesignatorObj != null && currentDesignatorObj != MySymbolTable.noObj) {
            accessArray = true;
            if (currentDesignatorObj.getKind() == Obj.Var) {
                if (currentDesignatorObj.getType().getKind() == Struct.Array) {
                    report_info("Element access of array '" + currentDesignatorObj.getName()
                        + "', objNode: [" + stringifyObjNode(currentDesignatorObj) + "]",
                        exprInBracketsDesignator);
                }
                else {
                    report_error("Variable '" + currentDesignatorObj.getName() +
                    "' is not of array type", exprInBracketsDesignator);
                }
            }
            else if (currentDesignatorObj.getKind() == Obj.Con) {
                report_error("Constant '" + currentDesignatorObj.getName() +
                "' cannot be of array type", exprInBracketsDesignator);
            }
        }
    }

    public void visit(InnerDotIdentDesignator innerDotIdentDesignator) {
        //TODO: implement class support
    }

    public void visit(FunctionCallStatement funcCallStatement) {
        if (currentDesignatorObj != MySymbolTable.noObj && !accessArray) {
            if (currentDesignatorObj.getKind() == Obj.Meth) {
                report_info("Function call of '" + currentDesignatorObj.getName() + "' objNode:[" +
                    stringifyObjNode(currentDesignatorObj) + "]", funcCallStatement);
            }
            else {
                report_error("Symbol '" + currentDesignatorObj.getName() + "' not a function",
                    funcCallStatement);
            }
        }
        else if (accessArray) {
            accessArray = false;
            report_error("Array Element cannot be function call", funcCallStatement);
        }
    }

    private void handleCurrentDesignator(SyntaxNode info) {
        if (currentDesignatorObj != MySymbolTable.noObj && !accessArray) {
            if (currentDesignatorObj.getKind() == Obj.Var) {
                if (currentDesignatorObj.getLevel() == 0) { // global
                    report_info("Usage of global variable '" + currentDesignatorObj.getName() 
                        + "', objNode: [" + stringifyObjNode(currentDesignatorObj) + "]",
                        info);
                }
                else {
                    if (currentMethodFormParams.containsKey(currentDesignatorObj.getName())) {
                        report_info("Usage of formal function argument '" + currentDesignatorObj.getName() 
                            + "', objNode: [" + stringifyObjNode(currentDesignatorObj) + "]",
                            info);
                    }
                    else {
                        report_info("Usage of local variable '" + currentDesignatorObj.getName() 
                            + "', objNode: [" + stringifyObjNode(currentDesignatorObj) + "]",
                            info);
                    }
                }
            }
            else if (currentDesignatorObj.getKind() == Obj.Con) {
                report_info("Usage of symbolic constant'" + currentDesignatorObj.getName() 
                            + "', objNode: [" + stringifyObjNode(currentDesignatorObj) + "]",
                            info);
            }
        }
        else {
            accessArray = false;
        }
    }

    public void visit(AssignDesignator assignDesignator) {
        handleCurrentDesignator(assignDesignator);
        currentDesignatorObj = null;
    }

    public void visit(ReadDesignator readDesignator) {
        handleCurrentDesignator(readDesignator);
        currentDesignatorObj = null;
    }

    public void visit(DesignatorFactor designatorFactor) {
        handleCurrentDesignator(designatorFactor);
        currentDesignatorObj = null;
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

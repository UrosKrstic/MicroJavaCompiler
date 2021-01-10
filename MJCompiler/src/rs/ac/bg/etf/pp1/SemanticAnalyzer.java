package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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

    Logger log = Logger.getLogger(getClass());

    public void report_error(String message, SyntaxNode info) {
        errorDetected = true;
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(", line ").append(line);
        log.error(msg.toString());
    }

    public void report_info(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(", line ").append(line);
        log.info(msg.toString());
    }

    public boolean passed() {
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
            report_info("Declared array variable '" + varDecl.getVarName() + "', symbol: " + "["
                    + stringifyObjNode(objNode) + "]", varDecl);
        } else { // is regular variable
            objNode = MySymbolTable.insert(Obj.Var, varDecl.getVarName(), currentType);
            report_info("Declared variable '" + varDecl.getVarName() + "', symbol: " + "[" + stringifyObjNode(objNode)
                    + "]", varDecl);
        }
        if (isFormParamDecl && objNode != null) {
            currentMethodFormParams.put(objNode.getName(), objNode);
        }
    }

    public void visit(ConstDecl constDecl) {
        if (checkForMultipleDeclarations(constDecl.getConstName(), 0, constDecl, "Constant name"))
            return;

        if (currentType.getKind() == Struct.Bool || currentType.getKind() == Struct.Int
                || currentType.getKind() == Struct.Char) {

            int constType = Struct.Int;
            if (constDecl.getConstValue() instanceof CharConst) {
                constType = Struct.Char;
            } else if (constDecl.getConstValue() instanceof BooleanConst) {
                constType = Struct.Bool;
            }

            if (constType == currentType.getKind()) {
                Obj objNode = MySymbolTable.insert(Obj.Con, constDecl.getConstName(), currentType);
                int constVal = 0;
                if (constDecl.getConstValue() instanceof CharConst) {
                    constVal = ((CharConst)constDecl.getConstValue()).getCharConst();
                } else if (constDecl.getConstValue() instanceof BooleanConst) {
                    constVal = ((BooleanConst)constDecl.getConstValue()).getBoolConst() ? 1 : 0;
                }
                else {
                    constVal = ((NumberConst)constDecl.getConstValue()).getNumConst();
                }
                objNode.setAdr(constVal);
                report_info("Declared constant '" + constDecl.getConstName() + "', symbol: " + "["
                        + stringifyObjNode(objNode) + "]", constDecl);
            } else {
                report_error("Invalid value for given type '" + currentTypeName + "' in constant declaration ",
                        constDecl);
            }
        } else {
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
        report_info("Function definition started: " + methodReturnTypeAndName.getMethodName(), methodReturnTypeAndName);
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

    public void visit(SingleCondition singleCondition) {
        singleCondition.obj = singleCondition.getCondTerm().obj;
    }

    public void visit(NextCondition nextCondition) {
        nextCondition.obj = nextCondition.getCondTerm().obj;
    }

    public void visit(SingleCondTerm singleCondTerm) {
        singleCondTerm.obj = singleCondTerm.getCondFact().obj;
    }

    public void visit(NextCondTerm nextCondTerm) {
        nextCondTerm.obj = nextCondTerm.getCondFact().obj;
    }

    public void visit(FullCondFact fullCondFact) {
        Struct struct1 = fullCondFact.getExpr().obj.getType();
        Struct struct2 = fullCondFact.getExpr1().obj.getType();

        if (struct1.compatibleWith(struct2)) {
            report_error("Types in relop must be compatible", fullCondFact);
        }
        if (struct1.isRefType() || struct2.isRefType()) {
            if (!(fullCondFact.getRelop() instanceof EqualsRelop)
                    && !(fullCondFact.getRelop() instanceof NotEqualsRelop)) {
                report_error("Referential types (classes and arrays) can only be compared with != and ==",
                        fullCondFact);
            }
        }
        fullCondFact.obj = new Obj(Obj.Var, "boolCond", MySymbolTable.boolType);
    }

    public void visit(SingleCondFact singleCondFact) {
        if (singleCondFact.obj.getType().getKind() != Struct.Bool) {
            report_error("Invalid type in condition for '" + singleCondFact.obj.getName() + 
                "', type must be bool", singleCondFact);
        }
        singleCondFact.obj = singleCondFact.getExpr().obj;
    }

    public void visit(TernaryExpr ternaryExpr) {
        if (!ternaryExpr.getTermExpr().obj.getType().equals(
            ternaryExpr.getTermExpr1().obj.getType())) {
            report_error("Second and third expr must be of equivalent types", ternaryExpr);
        }
        ternaryExpr.obj = ternaryExpr.getTermExpr().obj;
    }

    public void visit(RegularExpr regularExpr) {
        regularExpr.obj = regularExpr.getTermExpr().obj;
    }

    public void visit(SingleTermExpr singleTermExpr) {
        singleTermExpr.obj = singleTermExpr.getTerm().obj;
    }

    public void visit(SingleTermExprWithMinus singleTermExprWithMinus) {
        singleTermExprWithMinus.obj = singleTermExprWithMinus.getTerm().obj;
        if (singleTermExprWithMinus.obj.getType().getKind() != Struct.Int) {
            report_error("Invalid type in arithmetic expression, symbol '" + singleTermExprWithMinus.obj.getName()
                    + "', type must be int", singleTermExprWithMinus);
        }
    }

    public void visit(NextTermExpr nextTermExpr) {
        if (nextTermExpr.getTermExpr() instanceof SingleTermExpr) {
            if (nextTermExpr.getTermExpr().obj.getType().getKind() != Struct.Int) {
                report_error("Invalid type in arithmetic expression, symbol '" + nextTermExpr.getTerm().obj.getName()
                        + "', type must be int", nextTermExpr.getTerm());
            }
        }
        nextTermExpr.obj = nextTermExpr.getTerm().obj;
        if (nextTermExpr.obj.getType().getKind() != Struct.Int) {
            report_error("Invalid type in arithmetic expression, symbol '" + nextTermExpr.obj.getName()
                    + "', type must be int", nextTermExpr);
        }
    }

    public void visit(SingleTerm singleTerm) {
        singleTerm.obj = singleTerm.getFactor().obj;
    }

    public void visit(NextTerm nextTerm) {
        if (nextTerm.getTerm() instanceof SingleTerm) {
            if (nextTerm.getTerm().obj.getType().getKind() != Struct.Int) {
                report_error("Invalid type in arithmetic expression, symbol '" + nextTerm.getTerm().obj.getName()
                        + "', type must be int", nextTerm.getTerm());
            }
        }
        nextTerm.obj = nextTerm.getFactor().obj;
        if (nextTerm.obj.getType().getKind() != Struct.Int) {
            report_error(
                    "Invalid type in arithmetic expression, symbol '" + nextTerm.obj.getName() + "', type must be int",
                    nextTerm);
        }
    }

    public void visit(AssignDesignator assignDesignator) {
        boolean accessArray = assignDesignator.getDesignator() instanceof InnerExprInBracketsDesignator;
        handleCurrentDesignator(assignDesignator.getDesignator().obj, assignDesignator, accessArray);
        assignDesignator.obj = assignDesignator.getDesignator().obj;
    }

    public void visit(ReadDesignator readDesignator) {
        boolean accessArray = readDesignator.getDesignator() instanceof InnerExprInBracketsDesignator;
        handleCurrentDesignator(readDesignator.getDesignator().obj, readDesignator, accessArray);
        readDesignator.obj = readDesignator.getDesignator().obj;
    }

    public void visit(FunctionCall functionCall) {
        functionCall.obj = functionCall.getFunctionCallStatement().obj;
    }

    public void visit(ConstFactor constFactor) {
        if (constFactor.getConstValue() instanceof BooleanConst) {
            constFactor.obj = new Obj(Obj.Con, "constant", MySymbolTable.boolType);
        } else if (constFactor.getConstValue() instanceof CharConst) {
            constFactor.obj = new Obj(Obj.Con, "constant", MySymbolTable.charType);
        } else {
            constFactor.obj = new Obj(Obj.Con, "constant", MySymbolTable.intType);
        }
    }

    public void visit(NewOperatorFactor newOperatorFactor) {
        if (newOperatorFactor.obj.getType().getKind() != Struct.Class) {
            report_error("Invalid type in operator 'new', type must be a class", newOperatorFactor);
        }
        newOperatorFactor.obj = new Obj(Obj.Var, "newref", newOperatorFactor.getType().struct);
    }

    public void visit(NewOperatorFactorWithBrackets newOperatorFactor) {
        if (newOperatorFactor.getExpr().obj.getType().getKind() != Struct.Int) {
            report_error("Invalid type in brackets of operator 'new', type must be int", newOperatorFactor);
        }
        newOperatorFactor.obj = new Obj(Obj.Var, "newrefArr", newOperatorFactor.getType().struct);
    }

    public void visit(ExprFactor exprFactor) {
        exprFactor.obj = exprFactor.getExpr().obj;
    }

    public void visit(FuncCall funcCall) {
        funcCall.obj = funcCall.getFunctionCallStatement().obj;
    }

    public void visit(DesignatorFactor designatorFactor) {
        boolean accessArray = designatorFactor.getDesignator() instanceof InnerExprInBracketsDesignator;
        handleCurrentDesignator(designatorFactor.getDesignator().obj, designatorFactor, accessArray);
        designatorFactor.obj = designatorFactor.getDesignator().obj;
    }

    public void visit(SingleDesignator singleDesignator) {
        Obj currentDesignatorObj = MySymbolTable.find(singleDesignator.getName());
        if (currentDesignatorObj == MySymbolTable.noObj) {
            report_error("Undeclared symbol '" + singleDesignator.getName() + "'", singleDesignator);
        }
        singleDesignator.obj = currentDesignatorObj;
    }

    public void visit(InnerExprInBracketsDesignator exprInBracketsDesignator) {
        Obj currentDesignatorObj = exprInBracketsDesignator.getDesignator().obj;
        if (currentDesignatorObj != null && currentDesignatorObj != MySymbolTable.noObj) {
            if (currentDesignatorObj.getKind() == Obj.Var) {
                if (currentDesignatorObj.getType().getKind() == Struct.Array) {
                    if (exprInBracketsDesignator.getExpr().obj.getType().getKind() == Struct.Int) {
                        report_info("Element access of array '" + currentDesignatorObj.getName() + "', objNode: ["
                                + stringifyObjNode(currentDesignatorObj) + "]", exprInBracketsDesignator);
                        exprInBracketsDesignator.obj = new Obj(Obj.Var, "arrelem",
                                currentDesignatorObj.getType().getElemType());
                        return;
                    } else {
                        report_error(
                            "Invalid type for array '" + currentDesignatorObj.getName() + "' index, must be int",
                            exprInBracketsDesignator);
                    }
                } else {
                    report_error("Variable '" + currentDesignatorObj.getName() + "' is not of array type",
                        exprInBracketsDesignator);
                }
            } else if (currentDesignatorObj.getKind() == Obj.Con) {
                report_error("Constant '" + currentDesignatorObj.getName() + "' cannot be of array type",
                    exprInBracketsDesignator);
            }
        }
        exprInBracketsDesignator.obj = currentDesignatorObj;
    }

    public void visit(InnerDotIdentDesignator innerDotIdentDesignator) {
        // TODO: implement class support
    }

    public void visit(FunctionCallStatement funcCallStatement) {
        Obj currentDesignatorObj = funcCallStatement.getDesignator().obj;
        boolean accessArray = funcCallStatement.getDesignator() instanceof InnerExprInBracketsDesignator;
        if (currentDesignatorObj != MySymbolTable.noObj && 
            currentDesignatorObj.getName().equals("len") && !accessArray) {
            if (funcCallStatement.getOptionalActPars() instanceof FullActPars) {
                FullActPars fullActPars = (FullActPars) funcCallStatement.getOptionalActPars();
                ActPars actPars = fullActPars.getActPars();
                if (((SingleActPar)actPars).getExpr().obj.getType().getKind() == Struct.Array) {
                    report_info("Function call of '" + currentDesignatorObj.getName() + "' objNode:["
                        + stringifyObjNode(currentDesignatorObj) + "]", funcCallStatement);
                    funcCallStatement.obj = new Obj(Obj.Var, currentDesignatorObj.getName(),
                        currentDesignatorObj.getType());
                    return;
                }
                else {
                    report_error("Incorrect argument type for '" 
                        + ((SingleActPar)actPars).getExpr().obj.getName() + "' in function call of '" +
                        currentDesignatorObj.getName() + "'", funcCallStatement);
                    return;
                }
            }
            else {
                report_error("Incorrect number of function arguments in function call of '" + 
                    currentDesignatorObj.getName() + "'", funcCallStatement);
                return;
            }
        }
        if (currentDesignatorObj != MySymbolTable.noObj && !accessArray) {
            if (currentDesignatorObj.getKind() == Obj.Meth) {
                Collection<Obj> locals = currentDesignatorObj.getLocalSymbols();
                int formalArgCount = currentDesignatorObj.getLevel();
                ArrayList<Obj> actualArgs = new ArrayList<>();
                if (funcCallStatement.getOptionalActPars() instanceof FullActPars) {
                    FullActPars fullActPars = (FullActPars) funcCallStatement.getOptionalActPars();
                    ActPars actPars = fullActPars.getActPars();
                    while (actPars instanceof NextActPar) {
                        actualArgs.add(((NextActPar)actPars).getExpr().obj);
                        actPars = ((NextActPar)actPars).getActPars();
                    }
                    actualArgs.add(((SingleActPar)actPars).getExpr().obj);
                }

                if (actualArgs.size() != formalArgCount) {
                    report_error("Incorrect number of function arguments in function call of '" + 
                    currentDesignatorObj.getName() + "'", funcCallStatement);
                    return;
                }
                Iterator<Obj> iter = locals.iterator();
                boolean errorFound = false;
                for (int i = actualArgs.size() - 1; i >= 0; i--) {
                    Obj formalArg = iter.next();
                    if (!actualArgs.get(i).getType().assignableTo(formalArg.getType())) {
                        report_error("Incorrect argument type for '" + actualArgs.get(i).getName() +
                        "'(pos:" + Integer.toString(i) + ") in function call of '" +
                        currentDesignatorObj.getName() + "'", funcCallStatement);
                        errorFound = true;
                    }
                }
                if (errorFound) return;

                report_info("Function call of '" + currentDesignatorObj.getName() + "' objNode:["
                        + stringifyObjNode(currentDesignatorObj) + "]", funcCallStatement);
                funcCallStatement.obj = new Obj(Obj.Var, currentDesignatorObj.getName(),
                        currentDesignatorObj.getType());
            } else {
                report_error("Symbol '" + currentDesignatorObj.getName() + "' not a function", funcCallStatement);
                funcCallStatement.obj = new Obj(Obj.Var, currentDesignatorObj.getName(), MySymbolTable.noType);
            }
        } else if (accessArray) {
            report_error("Array Element cannot be function call", funcCallStatement);
        }
    }

    private void handleCurrentDesignator(Obj currentDesignatorObj, SyntaxNode info, boolean accessArray) {
        if (currentDesignatorObj != MySymbolTable.noObj && !accessArray) {
            if (currentDesignatorObj.getKind() == Obj.Var) {
                if (currentDesignatorObj.getLevel() == 0) { // global
                    report_info("Usage of global variable '" + currentDesignatorObj.getName() + "', objNode: ["
                            + stringifyObjNode(currentDesignatorObj) + "]", info);
                } else {
                    if (currentMethodFormParams.containsKey(currentDesignatorObj.getName())) {
                        report_info("Usage of formal function argument '" + currentDesignatorObj.getName()
                                + "', objNode: [" + stringifyObjNode(currentDesignatorObj) + "]", info);
                    } else {
                        report_info("Usage of local variable '" + currentDesignatorObj.getName() + "', objNode: ["
                                + stringifyObjNode(currentDesignatorObj) + "]", info);
                    }
                }
            } else if (currentDesignatorObj.getKind() == Obj.Con) {
                report_info("Usage of symbolic constant'" + currentDesignatorObj.getName() + "', objNode: ["
                        + stringifyObjNode(currentDesignatorObj) + "]", info);
            }
        }
    }

    public void visit(Type type) {
        Obj typeNode = MySymbolTable.find(type.getTypeName());
        currentTypeName = type.getTypeName();
        if (typeNode == MySymbolTable.noObj) {
            report_error("Type '" + type.getTypeName() + "' not found in symbol table", type);
            currentType = MySymbolTable.noType;
        } else {
            if (Obj.Type == typeNode.getKind()) {
                currentType = typeNode.getType();
            } else {
                report_error("Name '" + type.getTypeName() + "' is not a type", type);
                currentType = MySymbolTable.noType;
            }
        }
        type.struct = currentType;
    }
}

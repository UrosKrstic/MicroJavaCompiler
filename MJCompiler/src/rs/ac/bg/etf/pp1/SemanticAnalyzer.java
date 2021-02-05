package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class SemanticAnalyzer extends VisitorAdaptor {
    private static boolean errorDetected = false;

    // Declarations
    private static Struct currentType = MySymbolTable.noType;
    private static String currentTypeName = "noType";
    public static Map<Struct, Struct> tableOfArrayStructs = new LinkedHashMap<Struct, Struct>();

    // Class
    private static boolean inClassDefinition = false;
    private static boolean errorInClassDef = false;
    private static Obj currentClass = null;
    private Iterator<Obj> parentClassMemberIter = null;

    // Methods
    private static Obj currentMethod = null;
    private static boolean isFormParamDecl = false;
    private static Map<String, Obj> currentMethodFormParams = new LinkedHashMap<String, Obj>();

    //Statements
    private static int currentDoWhileCount = 0, currentSwitchCount = 0;

    //Counters
    public int globalDataCount = 0;

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

    private boolean isAssignable(Struct src, Struct dest) {
        report_info("LETS GO -1", null);
        if (src.getKind() != Struct.Class || dest.getKind() != Struct.Class) {
            return src.assignableTo(dest);
        }
        report_info("LETS GO 0", null);
        if (src.assignableTo(dest))
            return true;

        boolean isAssignable = false;
        Struct parentStruct = src.getElemType();
        report_info("LETS GO 1", null);
        while (parentStruct != null) {
            report_info("LETS GO 2", null);
            if (parentStruct.assignableTo(dest)) {
                report_info("LETS GO 3", null);
                isAssignable = true;
                break;
            }
            report_info("LETS GO 4", null);
            parentStruct = parentStruct.getElemType();
        }
        return isAssignable;
    }

    public void visit(ProgName progName) {
        progName.obj = MySymbolTable.insert(Obj.Prog, progName.getProgName(), MySymbolTable.noType);
        MySymbolTable.openScope();
    }

    public void visit(Program program) {
        Obj mainFunction = MySymbolTable.find("main");
        if (mainFunction.equals(MySymbolTable.noObj) || mainFunction.getKind() != Obj.Meth) {
            report_error("void main() global function missing in program", null);
        }
        globalDataCount = MySymbolTable.currentScope.getnVars();
        MySymbolTable.chainLocalSymbols(program.getProgName().obj);
        MySymbolTable.closeScope();
    }

    public void visit(VarDecl varDecl) {
        int level = currentMethod == null && !inClassDefinition ? 0 : 1;

        if (checkForMultipleDeclarations(varDecl.getVarName(), level, varDecl, "Variable name " + level))
            return;

        boolean isArray = varDecl.getOptionalArrayBrackets() instanceof ArrayBrackets;
        Obj objNode = null;
        int objKind = inClassDefinition && currentMethod == null ? Obj.Fld : Obj.Var;
        if (isArray) { // is array
            Struct arrStruct = tableOfArrayStructs.get(currentType);
            if (arrStruct == null) {
                arrStruct = new Struct(Struct.Array, currentType);
                tableOfArrayStructs.put(currentType, arrStruct);
            }
            objNode = MySymbolTable.insert(objKind, varDecl.getVarName(), arrStruct);
            report_info("Declared array variable '" + varDecl.getVarName() + "', symbol: " + "["
                    + stringifyObjNode(objNode) + "]", varDecl);
        }
        else if (currentType.getKind() == Struct.Class) {
            objNode = MySymbolTable.insert(objKind, varDecl.getVarName(), currentType);
            report_info("Declared object '" + varDecl.getVarName() + "' of a class, symbol: " +
                "[" + stringifyObjNode(objNode) + "]", varDecl);
        }
        else { // is regular variable
            objNode = MySymbolTable.insert(objKind, varDecl.getVarName(), currentType);
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
                    constVal = ((CharConst) constDecl.getConstValue()).getCharConst();
                } else if (constDecl.getConstValue() instanceof BooleanConst) {
                    constVal = ((BooleanConst) constDecl.getConstValue()).getBoolConst() ? 1 : 0;
                } else {
                    constVal = ((NumberConst) constDecl.getConstValue()).getNumConst();
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

    public void visit(ClassName className) {
        inClassDefinition = true;
        errorInClassDef = checkForMultipleDeclarations(className.getClassName(), 0, className, "Class name");
        if (errorInClassDef)
            return;

        // My own struct compares class types only by name
        MyStruct myClassStruct = new MyStruct(Struct.Class);
        currentClass = MySymbolTable.insert(Obj.Type, className.getClassName(), myClassStruct);
        myClassStruct.setObjNode(currentClass);

        className.obj = currentClass;
        MySymbolTable.openScope();
        // placeholder for VTP (used in code generation)
        MySymbolTable.insert(Obj.Fld, "", MySymbolTable.intType);
    }

    public void visit(ExtendedClass extendedClass) {
        if (errorInClassDef)
            return;
        Struct parentClassType = extendedClass.getType().struct;

        if (parentClassType.getKind() != Struct.Class) {
            report_error("Type after extends keyword must be a class type, in class '" + currentClass.getName()
                    + "' definition", extendedClass);
            errorInClassDef = true;
            return;
        }

        if (currentTypeName.equals(currentClass.getName())) {
            report_error("Class can't extend itself, in class '" + currentClass.getName() + "' definition",
                    extendedClass);
            errorInClassDef = true;
            return;
        }

        currentClass.getType().setElementType(parentClassType);
        int parentClassFieldCnt = parentClassType.getNumberOfFields() - 1;
        //int parentClassFieldCnt = parentClassType.getNumberOfFields();

        Collection<Obj> parentClassMembers = parentClassType.getMembers();
        parentClassMemberIter = parentClassMembers.iterator();

        // skipping VTP
        report_info("Parent class field count: " + stringifyObjNode(new Obj(Obj.Var, "ejl", parentClassType)), extendedClass);
        parentClassMemberIter.next();

        // inserting all fields from parent class
        for (int i = 0; i < parentClassFieldCnt; i++) {
            report_info("Parent field number: " + i, extendedClass);
            MySymbolTable.currentScope.addToLocals(parentClassMemberIter.next());
        }
    }

    public void visit(MethodDeclStart methodDeclStart) {
        MySymbolTable.chainLocalSymbols(currentClass.getType());
    }

    public void visit(ClassDecl classDecl) {
        if (errorInClassDef) {
            errorInClassDef = false;
            inClassDefinition = false;
            return;
        }

        Struct parentClassType = currentClass.getType().getElemType();

        if (parentClassType != null) {
            while (parentClassMemberIter.hasNext()) {
                Obj parentMethod = parentClassMemberIter.next();
                if (MySymbolTable.find(parentMethod.getName()).equals(Tab.noObj)) {
                    MySymbolTable.currentScope.addToLocals(parentMethod);
                }
            }
        }

        MySymbolTable.chainLocalSymbols(currentClass.getType());
        MySymbolTable.closeScope();
        inClassDefinition = false;
        currentClass = null;
    }

    public void visit(MethodReturnTypeAndName methodReturnTypeAndName) {
        Struct returnType = MySymbolTable.noType;
        if (methodReturnTypeAndName.getVoidOrType() instanceof ReturnType) {
            returnType = currentType;
        }
        currentMethod = MySymbolTable.insert(Obj.Meth, methodReturnTypeAndName.getMethodName(), returnType);
        methodReturnTypeAndName.obj = currentMethod;
        if (!inClassDefinition && currentMethod.getName().equals("main")) {
            if (!currentMethod.getType().equals(MySymbolTable.noType)) {
                report_error("Incorrect return type for global function main, type must be void",
                    methodReturnTypeAndName);
            }
        }
        MySymbolTable.openScope();
        // implicit this argument of a class method
        if (inClassDefinition) {
            Obj objThis = MySymbolTable.insert(Obj.Var, "this", currentClass.getType());
            currentMethodFormParams.put("this", objThis);
        }
        report_info("Function definition started: " + methodReturnTypeAndName.getMethodName(), methodReturnTypeAndName);
    }

    public void visit(MethodBodyStart methodBodyStart) {
        MySymbolTable.chainLocalSymbols(currentMethod);
        currentMethod.setLevel(currentMethodFormParams.size());
        if (!inClassDefinition && currentMethod.getName().equals("main")) {
            if (currentMethodFormParams.size() > 0) {
                report_error("Global function main does not have any arguments", methodBodyStart);
            }
        }
    }

    public void visit(MethodDecl methodDecl) {
        //MySymbolTable.chainLocalSymbols(currentMethod);
        MySymbolTable.closeScope();
        //currentMethod.setLevel(currentMethodFormParams.size());
        report_info("Function definition ended: " + stringifyObjNode(currentMethod), methodDecl);
        currentMethod = null;
        currentMethodFormParams.clear();
    }

    public void visit(AssignExpr assignExpr) {
        if (assignExpr.getAssignDesignator().obj.getKind() != Obj.Var &&
            assignExpr.getAssignDesignator().obj.getKind() != Obj.Fld &&
            assignExpr.getAssignDesignator().obj.getKind() != Obj.Elem) {
            report_error("Designator in assign statement must be a variable, array element or class field", 
                assignExpr);
        }
        report_info("WELL FINE", assignExpr);
        if (!isAssignable(assignExpr.getExpr().obj.getType(), assignExpr.getAssignDesignator().obj.getType())) {
            report_error("Types must be assign-compatible in assign expression", assignExpr);
        }
    }

    public void visit(PostIncrement postIncrement) {
        if (postIncrement.getAssignDesignator().obj.getKind() != Obj.Var &&
            postIncrement.getAssignDesignator().obj.getKind() != Obj.Fld &&
            postIncrement.getAssignDesignator().obj.getKind() != Obj.Elem) {
            report_error("Designator in post increment statement must be a variable, array element or class field", 
                postIncrement);
        }
        if (postIncrement.getAssignDesignator().obj.getType().getKind() != Struct.Int) {
            report_error("Designator in post increment statement must be of type int, char or bool", postIncrement);
        }
    }

    public void visit(PostDecrement postDecrement) {
        if (postDecrement.getAssignDesignator().obj.getKind() != Obj.Var &&
            postDecrement.getAssignDesignator().obj.getKind() != Obj.Fld &&
            postDecrement.getAssignDesignator().obj.getKind() != Obj.Elem) {
            report_error("Designator in post decrement statement must be a variable, array element or class field", 
                postDecrement);
        }
        if (postDecrement.getAssignDesignator().obj.getType().getKind() != Struct.Int) {
            report_error("Designator in post decrement statement must be of type int, char or bool", postDecrement);
        }
    }

    public void visit(SwitchStart switchStart) {
        currentSwitchCount++;
    }

    public void visit(SwitchEnd switchEnd) {
        currentSwitchCount--;
    }

    public void visit(SwitchStatement switchStatement) {
        Set<Integer> usedCases = new HashSet<>();
        if (switchStatement.getExpr().obj.getType().getKind() != Struct.Int) {
            report_error("Expression inside switch statement must be of type int", switchStatement);
        }
        CaseList currentCase = switchStatement.getCaseList();
        while (currentCase instanceof Case) {
            if (usedCases.contains(((Case)currentCase).getNumConst())) {
                report_error("Multiple case brances cannot have the same number constant", ((Case)currentCase));
            }
            else {
                usedCases.add(((Case)currentCase).getNumConst());
            }
            currentCase = ((Case)currentCase).getCaseList();
        }
    }

    public void visit(SimplePrintStatement simplePrintStatement) {
        if (simplePrintStatement.getExpr().obj.getType().isRefType()) {
            report_error("Invalid type in print statement; must be int, bool or char",
                simplePrintStatement);
        }
    }

    public void visit(ParameterizedPrintStatement ParameterizedPrintStatement) {
        if (ParameterizedPrintStatement.getExpr().obj.getType().isRefType()) {
            report_error("Invalid type in print statement; must be int, bool or char",
                ParameterizedPrintStatement);
        }
    }

    public void visit(DoWhileStart doWhileStart) {
        currentDoWhileCount++;
    }

    public void visit(DoWhileEnd doWhileEnd) {
        currentDoWhileCount--;
    }

    public void visit(BreakStatement breakStatement) {
        if (currentDoWhileCount == 0 && currentSwitchCount == 0) {
            report_error("Break statement must be inside of a do-while statement or a switch statement",
                breakStatement);
        }
    }

    public void visit(ContinueStatement continueStatement) {
        if (currentDoWhileCount == 0) {
            report_error("Continue statement must be inside of a do-while statement", continueStatement);
        }
    }

    public void visit(ReturnStatement returnStatement) {
        if (currentMethod == null) {
            report_error("Return statement must be inside of a global function or class method", 
                returnStatement);
            return;
        }
        if (returnStatement.getOptionalExpr() instanceof NoExpr) {
            if (!currentMethod.getType().equals(MySymbolTable.noType)) {
                report_error("Missing expression in return statement", returnStatement);
            }
        }
        else {
            if (!((FullExpr)returnStatement.getOptionalExpr()).getExpr().obj.getType().
                equals(currentMethod.getType())) {
                report_error("Invalid type in return statement", returnStatement);
            }
        }
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

        if (!struct1.compatibleWith(struct2)) {
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
        singleCondFact.obj = singleCondFact.getExpr().obj;
        if (singleCondFact.obj.getType().getKind() != Struct.Bool) {
            report_error("Invalid type in condition, type must be bool", singleCondFact);
        }
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

        if (readDesignator.getDesignator().obj.getKind() != Obj.Var &&
        readDesignator.getDesignator().obj.getKind() != Obj.Fld) {
            report_error("Designator in read statement must be a variable, array element or class field", 
                readDesignator);
        }
        if (readDesignator.getDesignator().obj.getType().isRefType()) {
            report_error("Designator in read statement must be of type int, char or bool", readDesignator);
        }
    }

    public void visit(FunctionCall functionCall) {
        functionCall.obj = functionCall.getFunctionCallStatement().obj;
    }

    public void visit(ConstFactor constFactor) {
        int constVal = 0;

        if (constFactor.getConstValue() instanceof BooleanConst) {
            constFactor.obj = new Obj(Obj.Con, "constant", MySymbolTable.boolType);
            constVal = ((BooleanConst) constFactor.getConstValue()).getBoolConst() ? 1 : 0;
        } else if (constFactor.getConstValue() instanceof CharConst) {
            constFactor.obj = new Obj(Obj.Con, "constant", MySymbolTable.charType);
            constVal = ((CharConst) constFactor.getConstValue()).getCharConst();
        } else {
            report_info("Lets go int bby", constFactor);
            constFactor.obj = new Obj(Obj.Con, "constant", MySymbolTable.intType);
            constVal = ((NumberConst) constFactor.getConstValue()).getNumConst();
        }

        constFactor.obj.setAdr(constVal);
    }

    public void visit(NewOperatorFactor newOperatorFactor) {
        if (newOperatorFactor.getType().struct.getKind() != Struct.Class) {
            report_error("Invalid type in operator 'new', type must be a class", newOperatorFactor);
        }
        report_info("LETS GOOOOOO", newOperatorFactor);
        newOperatorFactor.obj = new Obj(Obj.Var, "newref", newOperatorFactor.getType().struct);
        report_info(stringifyObjNode(newOperatorFactor.obj), newOperatorFactor);
    }

    public void visit(NewOperatorFactorWithBrackets newOperatorFactor) {
        if (newOperatorFactor.getExpr().obj.getType().getKind() != Struct.Int) {
            report_error("Invalid type in brackets of operator 'new', type must be int", newOperatorFactor);
        }
        newOperatorFactor.obj = new Obj(Obj.Var, "newrefArr", tableOfArrayStructs.get(newOperatorFactor.getType().struct)); 
        report_info("[new operator arr] " + newOperatorFactor.obj.getType().getKind() +
        newOperatorFactor.obj.getType().getElemType().getKind(), newOperatorFactor);
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
        if (currentDesignatorObj.equals(MySymbolTable.noObj)) {
            if (inClassDefinition && currentMethod != null) {
                Struct parentType = currentClass.getType().getElemType();
                if (parentType != null) {
                    Obj tmp = parentType.getMembersTable().searchKey(singleDesignator.getName());
                    if (tmp != null) currentDesignatorObj = tmp;
                    else {
                        report_error("Undeclared symbol '" + singleDesignator.getName() + "'", singleDesignator);
                    }
                }
                else {
                    report_error("Undeclared symbol '" + singleDesignator.getName() + "'", singleDesignator);
                }
            }
            else {
                report_error("Undeclared symbol '" + singleDesignator.getName() + "'", singleDesignator);
            }
        }
        singleDesignator.obj = currentDesignatorObj;
    }

    public void visit(InnerExprInBracketsDesignator exprInBracketsDesignator) {
        Obj currentDesignatorObj = exprInBracketsDesignator.getDesignator().obj;
        if (currentDesignatorObj != null && !currentDesignatorObj.equals(MySymbolTable.noObj)) {
            if (currentDesignatorObj.getKind() == Obj.Var || currentDesignatorObj.getKind() == Obj.Fld) {
                if (currentDesignatorObj.getType().getKind() == Struct.Array) {
                    if (exprInBracketsDesignator.getExpr().obj.getType().getKind() == Struct.Int) {
                        report_info("Element access of array '" + currentDesignatorObj.getName() + "', objNode: ["
                                + stringifyObjNode(currentDesignatorObj) + "]", exprInBracketsDesignator);
                        exprInBracketsDesignator.obj = new Obj(Obj.Elem, "arrelem",
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
        Obj currentDesignatorObj = innerDotIdentDesignator.getDesignator().obj;
        if (currentDesignatorObj != null && !currentDesignatorObj.equals(MySymbolTable.noObj)) {
            // if (currentDesignatorObj.getName().equals("this")) {
            //     report_info("Its this ffs " + currentDesignatorObj.getType().getKind(), innerDotIdentDesignator);
            // }
            if (currentDesignatorObj.getType().getKind() == Struct.Class) {
                Struct classType = currentDesignatorObj.getType();
                Obj tmp = classType.getMembersTable().searchKey(innerDotIdentDesignator.getClassField());
                // locates non redefined parent methods, allows for them to be called
                if (tmp == null && classType.getElemType() != null)
                    tmp = classType.getElemType().getMembersTable().
                        searchKey(innerDotIdentDesignator.getClassField());
                if (tmp != null) {
                    currentDesignatorObj = tmp;
                    if (innerDotIdentDesignator.getDesignator() instanceof SingleDesignator ||
                        innerDotIdentDesignator.getDesignator() instanceof InnerDotIdentDesignator) {
                        handleCurrentDesignator(innerDotIdentDesignator.getDesignator().obj,
                            innerDotIdentDesignator.getDesignator(), false);
                    }
                    // report_info("Usage of a class field '" + currentDesignatorObj.getName() + "', objNode: ["
                    //     + stringifyObjNode(currentDesignatorObj) + "]", innerDotIdentDesignator);
                }
                else {
                    report_error("Undefined field accessed, at '" +
                    innerDotIdentDesignator.getClassField() + "'", innerDotIdentDesignator);
                }
            }
            else {
                report_error("Type of the identifier must be a class to allow field access, in '" +
                    currentDesignatorObj.getName() + "'", innerDotIdentDesignator.getDesignator());
            }
        }
        innerDotIdentDesignator.obj = currentDesignatorObj;
    }

    public void visit(FunctionCallStatement funcCallStatement) {
        Obj currentDesignatorObj = funcCallStatement.getDesignator().obj;
        funcCallStatement.obj = funcCallStatement.getDesignator().obj;
        boolean accessArray = funcCallStatement.getDesignator() instanceof InnerExprInBracketsDesignator;
        if (!currentDesignatorObj.equals(MySymbolTable.noObj) &&
            currentDesignatorObj.getName().equals("len") && !accessArray &&
            currentDesignatorObj.getLevel() == 0) {
            // funcCallStatement.obj = new Obj(Obj.Var, currentDesignatorObj.getName(),
            // currentDesignatorObj.getType());
            if (funcCallStatement.getOptionalActPars() instanceof FullActPars) {
                FullActPars fullActPars = (FullActPars) funcCallStatement.getOptionalActPars();
                ActPars actPars = fullActPars.getActPars();
                if (((SingleActPar)actPars).getExpr().obj.getType().getKind() == Struct.Array) {
                    report_info("Function call of '" + currentDesignatorObj.getName() + "' objNode:["
                        + stringifyObjNode(currentDesignatorObj) + "]", funcCallStatement);
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
        if (!currentDesignatorObj.equals(MySymbolTable.noObj) && !accessArray) {
            if (currentDesignatorObj.getKind() == Obj.Meth) {
                boolean isClassMethod = false;
                Collection<Obj> locals = currentDesignatorObj.getLocalSymbols();
                int formalArgCount = currentDesignatorObj.getLevel();
                // cheat way of getting info if func call is class method call
                // by checking for this hidden formal arg
                if (formalArgCount > 0) {
                    isClassMethod = locals.iterator().next().getName().equals("this");
                    // if (isClassMethod)
                    //     report_info("It is a class method get that this out of here goddamnit", funcCallStatement);
                }
                if (isClassMethod) formalArgCount--;
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
                if (isClassMethod) iter.next();
                boolean errorFound = false;
                for (int i = actualArgs.size() - 1; i >= 0; i--) {
                    Obj formalArg = iter.next();
                    // if (!actualArgs.get(i).getType().assignableTo(formalArg.getType())) {
                    if (!isAssignable(actualArgs.get(i).getType(), formalArg.getType())) {
                        report_error("Incorrect argument type for '" + actualArgs.get(i).getName() +
                        "'(pos:" + Integer.toString(i) + ") in function call of '" +
                        currentDesignatorObj.getName() + "'", funcCallStatement);
                        errorFound = true;
                    }
                }
                if (errorFound) return;

                if (isClassMethod) {
                    report_info("Class method call of '" + currentDesignatorObj.getName() + "' objNode:["
                        + stringifyObjNode(currentDesignatorObj) + "]", funcCallStatement);
                    // funcCallStatement.obj = new Obj(Obj.Fld, currentDesignatorObj.getName(),
                    //     currentDesignatorObj.getType());
                }
                else {
                    report_info("Function call of '" + currentDesignatorObj.getName() + "' objNode:["
                        + stringifyObjNode(currentDesignatorObj) + "]", funcCallStatement);
                    // funcCallStatement.obj = new Obj(Obj.Var, currentDesignatorObj.getName(),
                    //     currentDesignatorObj.getType());
                }
            } else {
                report_error("Symbol '" + currentDesignatorObj.getName() + "' not a function", funcCallStatement);
                // funcCallStatement.obj = new Obj(Obj.Var, currentDesignatorObj.getName(), MySymbolTable.noType);
            }
        } else if (accessArray) {
            report_error("Array Element cannot be function call", funcCallStatement);
        }
    }

    private void handleCurrentDesignator(Obj currentDesignatorObj, SyntaxNode info, boolean accessArray) {
        if (!currentDesignatorObj.equals(MySymbolTable.noObj) && !accessArray) {
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
            else if (currentDesignatorObj.getKind() == Obj.Fld) {
                report_info("Usage of a class field '" + currentDesignatorObj.getName() + "', objNode: ["
                            + stringifyObjNode(currentDesignatorObj) + "]", info);
            }
        }
    }

    public void visit(Type type) {
        Obj typeNode = MySymbolTable.find(type.getTypeName());
        currentTypeName = type.getTypeName();
        if (typeNode.equals(MySymbolTable.noObj)) {
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

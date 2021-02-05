package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;

public class CodeGenerator extends VisitorAdaptor {
    public int mainPc;

    private static Logger log = Logger.getLogger(CodeGenerator.class);

    private LinkedList<Obj> definedClasses = new LinkedList<>();
    private Map<Struct, Integer> vtfPointers  = new LinkedHashMap<>();
    private boolean inClassDefinition = false;

    private void createVTF() {
        int staticDataMemoryCurrLoc = Code.dataSize;
        for (Obj classObj : definedClasses) {
            report_info("[VTF]" + stringifyObjNode(classObj), null);
            vtfPointers.put(classObj.getType(), staticDataMemoryCurrLoc);
            Iterator<Obj> objIter =  classObj.getType().getMembers().iterator();
            int fieldCount = classObj.getType().getNumberOfFields();

            // skip all class fields
            for (int i = 0; i < fieldCount; i++) objIter.next();

            // add each method name and address in vtf
            while (objIter.hasNext()) {
                Obj methObj = objIter.next();
                // add method name char by char
                for (int i = 0; i < methObj.getName().length(); i++) {
                    Code.loadConst(methObj.getName().charAt(i));
                    Code.put(Code.putstatic);
                    Code.put2(staticDataMemoryCurrLoc++);
                }
                // add terminal char of method name
                Code.loadConst(-1);
                Code.put(Code.putstatic);
                Code.put2(staticDataMemoryCurrLoc++);

                // add address of method
                Code.loadConst(methObj.getAdr());
                Code.put(Code.putstatic);
                Code.put2(staticDataMemoryCurrLoc++);
            }
            Code.loadConst(-2);
            Code.put(Code.putstatic);
            Code.put2(staticDataMemoryCurrLoc++);
        }
        Code.dataSize = staticDataMemoryCurrLoc;
    }

    private void createBuiltinFunctions() {
        Obj chrMethObj = MySymbolTable.find("chr");
        report_info("[CHR]" + stringifyObjNode(chrMethObj), null);
        chrMethObj.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(1);
        Code.put(1);

        Code.put(Code.load);
        Code.put(0);

        Code.put(Code.exit);
        Code.put(Code.return_);


        Obj ordMethObj = MySymbolTable.find("ord");
        report_info("[ORD]" + stringifyObjNode(ordMethObj), null);
        ordMethObj.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(1);
        Code.put(1);

        Code.put(Code.load);
        Code.put(0);

        Code.put(Code.exit);
        Code.put(Code.return_);


        Obj lenMethObj = MySymbolTable.find("len");
        report_info("[LEN]" + stringifyObjNode(lenMethObj), null);
        lenMethObj.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(1);
        Code.put(1);

        Code.put(Code.load);
        Code.put(0);
        Code.put(Code.arraylength);

        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void report_info(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(", line ").append(line);
        log.info(msg.toString());
    }

    public String stringifyObjNode(Obj objNode) {
        MyDumpSymbolTableVisitor visitor = new MyDumpSymbolTableVisitor();
        objNode.accept(visitor);
        return visitor.getOutput();
    }

    public void visit(ProgName progName) {
        // createBuiltinFunctions();
    }

    public void visit(ClassName className) {
        inClassDefinition = true;
        definedClasses.add(className.obj);
    }

    public void visit(ClassDecl classDecl) {
        inClassDefinition = false;
    }

    public void visit(MethodReturnTypeAndName methodReturnTypeAndName) {
        methodReturnTypeAndName.obj.setAdr(Code.pc);
        report_info("[Method Decl] " + stringifyObjNode(methodReturnTypeAndName.obj),
            methodReturnTypeAndName);

        if ("main".equals(methodReturnTypeAndName.getMethodName())) {
            mainPc = Code.pc;
            createVTF();
        }

        int formalParamCount = methodReturnTypeAndName.obj.getLevel();
        int localVarCount = methodReturnTypeAndName.obj.getLocalSymbols().size() - formalParamCount;
        report_info("[Method definition] fpcnt: " + formalParamCount + ", lvcnt: " + localVarCount, methodReturnTypeAndName);

        Code.put(Code.enter);
        Code.put(formalParamCount);
        Code.put(formalParamCount + localVarCount);
    }

    public void visit(ReturnStatement returnStatement) {
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void visit(MethodDecl methodDecl) {
        if (!methodDecl.getMethodReturnTypeAndName().obj.getType().equals(MySymbolTable.noType)) {
            Code.put(Code.trap);
            Code.put(1);
        }
        else {
            Code.put(Code.exit);
            Code.put(Code.return_);
        }
    }

    private void prepareCall(FunctionCallStatement functionCallStatement) {
        // cheat way of getting info if func call is class method call
        // by checking for this hidden formal arg
        boolean isClassMethod = false;
        Collection<Obj> locals = functionCallStatement.obj.getLocalSymbols();
        int formalArgCount = functionCallStatement.obj.getLevel();
        if (formalArgCount > 0) {
            isClassMethod = locals.iterator().next().getName().equals("this");
        }

        if (!isClassMethod) {
            int oldPc = Code.pc;
            Code.put(Code.call);
            Code.put2(functionCallStatement.obj.getAdr() - oldPc);
        }
        else {
            // traverse once more to reload the object of class
            functionCallStatement.getDesignator().traverseBottomUp(new CodeGenerator());

            //reload manually in case of single designator
            if (functionCallStatement.getDesignator() instanceof SingleDesignator) {
                Code.put(Code.load);
                Code.put(0);
            }

            // add VTF pointer of current class object
            Code.put(Code.getfield);
            Code.put2(0);

            // put invokevirtual call of method
            Code.put(Code.invokevirtual);
            String methodName = functionCallStatement.obj.getName();
            for (int i = 0; i < methodName.length(); i++) {
                Code.put4(methodName.charAt(i));
            }
            Code.put4(-1);
        }
    }

    public void visit(FunctionCall functionCall) {
        prepareCall(functionCall.getFunctionCallStatement());
        if (!functionCall.getFunctionCallStatement().obj.getType().equals(MySymbolTable.noType))
                Code.put(Code.pop);
    }

    public void visit(FuncCall funcCall) {
        prepareCall(funcCall.getFunctionCallStatement());
    }

    public void visit(AssignExpr assignExpr) {
        Code.store(assignExpr.getAssignDesignator().obj);
    }

    public void visit(SimplePrintStatement simplePrintStatement) {
        if (!simplePrintStatement.getExpr().obj.getType().equals(MySymbolTable.charType)) {
            Code.loadConst(5);
            Code.put(Code.print);
        }
        else {
            Code.loadConst(1);
            Code.put(Code.bprint);
        }
    }

    public void visit(ParameterizedPrintStatement parameterizedPrintStatement) {
        Code.loadConst(parameterizedPrintStatement.getPrintWidth());
        if (!parameterizedPrintStatement.getExpr().obj.getType().equals(MySymbolTable.charType)) {
            Code.put(Code.print);
        }
        else {
            Code.put(Code.bprint);
        }
    }

    public void visit(PostIncrement postIncrement) {
        postIncrement.getAssignDesignator().getDesignator().traverseBottomUp(new CodeGenerator());

        if (postIncrement.getAssignDesignator().getDesignator() instanceof SingleDesignator) {
            Code.load(postIncrement.getAssignDesignator().obj);
        }

        Code.loadConst(1);
        Code.put(Code.add);
        Code.store(postIncrement.getAssignDesignator().obj);
    }

    public void visit(PostDecrement postDecrement) {
        postDecrement.getAssignDesignator().getDesignator().traverseBottomUp(new CodeGenerator());

        if (postDecrement.getAssignDesignator().getDesignator() instanceof SingleDesignator) {
            Code.load(postDecrement.getAssignDesignator().obj);
        }

        Code.loadConst(1);
        Code.put(Code.sub);
        Code.store(postDecrement.getAssignDesignator().obj);
    }

    private int getInverseRelopCode(FullCondFact fullCondFact) {
        Relop relop = fullCondFact.getRelop();
        int relopCode = 0;

        if (relop instanceof EqualsRelop) {
            relopCode = Code.inverse[Code.eq];
        }
        else if (relop instanceof NotEqualsRelop) {
            relopCode = Code.inverse[Code.ne];
        }
        else if (relop instanceof GreaterRelop) {
            relopCode = Code.inverse[Code.gt];
        }
        else if (relop instanceof GreaterEqualsRelop) {
            relopCode = Code.inverse[Code.ge];
        }
        else if (relop instanceof LesserRelop) {
            relopCode = Code.inverse[Code.lt];
        }
        else if (relop instanceof LesserEqualsRelop) {
            relopCode = Code.inverse[Code.le];
        }

        return relopCode;
    }

    private void setRelOp(CondFact condFact, int startOfIfAddr, int endOfIfAddr, boolean orOpExists, 
        boolean andOpExists, int prevRelOpAddr) {
        int relOpStartAddr = condFact.obj.getKind();
        if (condFact instanceof SingleCondFact) {
            if (andOpExists) {
                Code.put2(relOpStartAddr, (Code.jcc + Code.eq) << 8); // inverse
                Code.put2(relOpStartAddr + 1, prevRelOpAddr - relOpStartAddr);
            }
            else if (orOpExists) {
                Code.put2(relOpStartAddr, (Code.jcc + Code.gt) << 8); // inverse of inverse
                Code.put2(relOpStartAddr + 1, startOfIfAddr - relOpStartAddr);
            }
            else {
                Code.put2(relOpStartAddr, (Code.jcc + Code.eq) << 8); // inverse
                Code.put2(relOpStartAddr + 1, endOfIfAddr - relOpStartAddr);
            }
        }
        else {
            int relopCode = getInverseRelopCode((FullCondFact)condFact);
            if (andOpExists) {
                Code.put2(relOpStartAddr, (Code.jcc + relopCode) << 8); // inverse
                Code.put2(relOpStartAddr + 1, prevRelOpAddr - relOpStartAddr);
            }
            else if (orOpExists) {
                Code.put2(relOpStartAddr, (Code.jcc + Code.inverse[relopCode]) << 8); // inverse of inverse
                Code.put2(relOpStartAddr + 1, startOfIfAddr - relOpStartAddr);
            }
            else {
                Code.put2(relOpStartAddr, (Code.jcc + relopCode) << 8); // inverse
                Code.put2(relOpStartAddr + 1, endOfIfAddr - relOpStartAddr);
            }
        }
    }

    private int handleCondTerm(CondTerm condTerm, int startOfIfAddr, int endOfIfAddr,
        boolean orOpExists, boolean isFirstTerm) {
        boolean andOpExists = false;
        int prevRelOpAddr = 0;

        if (condTerm instanceof NextCondTerm) {
            andOpExists = true;
            CondFact condFact = ((NextCondTerm)condTerm).getCondFact();
            int relOpStartAddr = condFact.obj.getKind();
            prevRelOpAddr = relOpStartAddr + 3;

            if (isFirstTerm) {
                prevRelOpAddr = endOfIfAddr;
                if (condFact instanceof SingleCondFact) {
                    Code.put2(relOpStartAddr, (Code.jcc + Code.eq) << 8); // inverse
                    Code.put2(relOpStartAddr + 1, endOfIfAddr - relOpStartAddr);
                }
                else {
                    int relopCode = getInverseRelopCode((FullCondFact)condFact);
                    Code.put2(relOpStartAddr, (Code.jcc + relopCode) << 8); // inverse
                    Code.put2(relOpStartAddr + 1, endOfIfAddr - relOpStartAddr);
                }
            }
            else {
                if (condFact instanceof SingleCondFact) {
                    Code.put2(relOpStartAddr, (Code.jcc + Code.gt) << 8); // inverse of inverse
                    Code.put2(relOpStartAddr + 1, startOfIfAddr - relOpStartAddr);
                }
                else {
                    int relopCode = getInverseRelopCode((FullCondFact)condFact); // inverse of inverse
                    Code.put2(relOpStartAddr, (Code.jcc + Code.inverse[relopCode]) << 8);
                    Code.put2(relOpStartAddr + 1, startOfIfAddr - relOpStartAddr);
                }
            }
            condTerm = ((NextCondTerm)condTerm).getCondTerm();
        }

        while (condTerm instanceof NextCondTerm) {
            setRelOp(((NextCondTerm)condTerm).getCondFact(), startOfIfAddr, endOfIfAddr,
                orOpExists, andOpExists, prevRelOpAddr);
            condTerm = ((NextCondTerm)condTerm).getCondTerm();
        }

        setRelOp(((SingleCondTerm)condTerm).getCondFact(), startOfIfAddr, endOfIfAddr, 
            orOpExists, andOpExists, prevRelOpAddr);
        return ((SingleCondTerm)condTerm).getCondFact().obj.getKind();
    }

    private void handleConditionalStatement(Condition condition, int startOfStatement, int endOfStatement) {
        boolean orOpExists = false;
        boolean andOpExists = false;
        boolean firstTerm = false;

        if (condition instanceof NextCondition) {
            orOpExists = true;
            CondTerm condTerm = ((NextCondition)condition).getCondTerm();
            CondFact rightMostCondFact;
            boolean isSingleCondTerm = false;

            if (condTerm instanceof NextCondTerm) {
                andOpExists = true;
                rightMostCondFact = ((NextCondTerm)condTerm).getCondFact();
                condTerm = ((NextCondTerm)condTerm).getCondTerm();
            }
            else {
                isSingleCondTerm = true;
                rightMostCondFact = ((SingleCondTerm)condTerm).getCondFact();
            }

            int relOpStartAddr = rightMostCondFact.obj.getKind();

            // right most cond fact
            if (rightMostCondFact instanceof SingleCondFact) {
                Code.put2(relOpStartAddr, (Code.jcc + Code.eq) << 8);
                Code.put2(relOpStartAddr + 1, endOfStatement - relOpStartAddr);
            }
            else {
                int relopCode = getInverseRelopCode((FullCondFact)rightMostCondFact);
                Code.put2(relOpStartAddr, (Code.jcc + relopCode) << 8);
                Code.put2(relOpStartAddr + 1, endOfStatement - relOpStartAddr);
            }

            while (condTerm instanceof NextCondTerm) {
                setRelOp(((NextCondTerm)condTerm).getCondFact(), startOfStatement, endOfStatement, 
                    orOpExists, andOpExists, endOfStatement);
                condTerm = ((NextCondTerm)condTerm).getCondTerm();
            }

            if (!isSingleCondTerm) {
                setRelOp(((SingleCondTerm)condTerm).getCondFact(), startOfStatement, endOfStatement, 
                    orOpExists, andOpExists, endOfStatement);
            }

            condition = ((NextCondition)condition).getCondition();
            andOpExists = false;
        }
        else {
            firstTerm = true;
        }

        while (condition instanceof NextCondition) {
            handleCondTerm(((NextCondition)condition).getCondTerm(), startOfStatement,
                endOfStatement, orOpExists, firstTerm);
            if (firstTerm) firstTerm = false;
            condition = ((NextCondition)condition).getCondition();
        }

        handleCondTerm(((SingleCondition)condition).getCondTerm(), startOfStatement,
            endOfStatement, orOpExists, firstTerm);
    }

    private Stack<Integer> doWhileStartAddrs = new Stack<>();
    private Stack<Integer> switchStartAddrs = new Stack<>();
    private Map<Integer, LinkedList<Integer>> breaksInStatements = new HashMap<>();
    private Stack<Boolean> insideDoWhile = new Stack<>();

    public void visit(DoWhileStatement doWhileStatement) {
        int startOfWhileAddr = doWhileStartAddrs.pop();
        insideDoWhile.pop();
        Code.putJump(startOfWhileAddr);
        int endOfWhileAddr = Code.pc;

        // adding the end of do-while address to all inner break statements
        LinkedList<Integer> breakAddrs = breaksInStatements.get(startOfWhileAddr);
        for (int breakAddr : breakAddrs) {
            Code.put2(breakAddr + 1, endOfWhileAddr - breakAddr);
        }
        breaksInStatements.remove(startOfWhileAddr);

        handleConditionalStatement(doWhileStatement.getCondition(), startOfWhileAddr, endOfWhileAddr);
    }

    public void visit(DoWhileStart doWhileStart) {
        doWhileStartAddrs.push(Code.pc);
        breaksInStatements.put(Code.pc, new LinkedList<Integer>());
        insideDoWhile.push(true);
        // report_info("pc of start do-while: " + Code.pc, doWhileStart);
    }

    public void visit(SwitchStatement switchStatement) {
        int startOfSwitchAddr = switchStartAddrs.pop();
        insideDoWhile.pop();
        int endOfSwitchAddr = Code.pc;

        // adding the end of switch address to all inner break statements
        LinkedList<Integer> breakAddrs = breaksInStatements.get(startOfSwitchAddr);
        for (int breakAddr : breakAddrs) {
            Code.put2(breakAddr + 1, endOfSwitchAddr - breakAddr);
        }
        breaksInStatements.remove(startOfSwitchAddr);
    }

    public void visit(SwitchStart switchStart) {
        switchStartAddrs.push(Code.pc);
        breaksInStatements.put(Code.pc, new LinkedList<Integer>());
        insideDoWhile.push(false);
    }

    public void visit(BreakStatement breakStatement) {
        if (insideDoWhile.peek()) {
            breaksInStatements.get(doWhileStartAddrs.peek()).add(Code.pc);
        }
        else {
            breaksInStatements.get(switchStartAddrs.peek()).add(Code.pc);
        }
        Code.putJump(0);
    }

    public void visit(ContinueStatement continueStatement) {
        Code.putJump(doWhileStartAddrs.peek());
    }

    public void visit(IfElseStatement ifElseStatement) {
        Condition condition = ((RegularIfCondExpr)ifElseStatement.getIfCondExpr()).getCondition();
        int startOfIfAddr = ifElseStatement.getIfCondExpr().obj.getKind();
        int endOfIfAddr =  ifElseStatement.getOptionalElseStatement().obj.getKind();
        if (ifElseStatement.getOptionalElseStatement() instanceof ElseStatement)
            Code.put2(endOfIfAddr - 2, Code.pc - endOfIfAddr + 3);
        handleConditionalStatement(condition, startOfIfAddr, endOfIfAddr);
    }

    public void visit(TernaryExpr ternaryExpr) {
        int ternaryRelOpAddr = ternaryExpr.getTernaryFirstExpr().obj.getKind();
        int ternaryStartAddrOfThirdExpr = ternaryExpr.getTernaryThirdExprStart().obj.getKind();
        Code.put2(ternaryRelOpAddr + 1, ternaryStartAddrOfThirdExpr - ternaryRelOpAddr);
        Code.put2(ternaryStartAddrOfThirdExpr - 2, Code.pc - ternaryStartAddrOfThirdExpr + 3);
    }

    public void visit(TernaryFirstExpr ternaryFirstExpr) {
        Code.loadConst(0);
        ternaryFirstExpr.obj = new Obj(Code.pc, "", null);
        Code.put(Code.jcc + Code.eq);
        Code.put2(0);
    }

    public void visit(TernaryThirdExprStart ternaryThirdExprStart) {
        Code.putJump(0);
        ternaryThirdExprStart.obj = new Obj(Code.pc, "", null);
    }

    public void visit(RegularIfCondExpr ifCondExpr) {
        ifCondExpr.obj = new Obj(Code.pc, "", null);
    }

    public void visit(NoElseStatement noElseStatement) {
        noElseStatement.obj = new Obj(Code.pc, "", null);
    }

    public void visit(ElseStatement elseStatement) {
        elseStatement.obj = elseStatement.getElseKeyword().obj;
    }

    public void visit(ElseKeyword elseKeyword) {
        Code.putJump(0);
        elseKeyword.obj = new Obj(Code.pc, "", null);
    }

    public void visit(FullCondFact fullCondFact) {
        fullCondFact.obj = new Obj(Code.pc, fullCondFact.obj.getName(), fullCondFact.obj.getType());
        Code.putFalseJump(Code.eq, 0); // filler code, to leave space for later
    }

    public void visit(SingleCondFact singleCondFact) {
        Code.loadConst(0); // must compare to smth in case of a single boolean value on stack
        singleCondFact.obj = new Obj(Code.pc, singleCondFact.obj.getName(), singleCondFact.obj.getType());
        Code.putFalseJump(Code.eq, 0); // filler code, to leave space for later
    }

    public void visit(SingleTermExprWithMinus singleTermExprWithMinus) {
        Code.put(Code.neg);
    }

    public void visit(NextTermExpr nextTermExpr) {
        if (nextTermExpr.getAddop() instanceof AddAddop) {
            Code.put(Code.add);
        }
        else {
            Code.put(Code.sub);
        }
    }

    public void visit(NextTerm nextTerm) {
        if (nextTerm.getMulop() instanceof MulMulop) { // MUL
            Code.put(Code.mul);
        }
        else if (nextTerm.getMulop() instanceof DivMulop) { // DIV
            Code.put(Code.div);
        }
        else { // MOD
            Code.put(Code.rem);
        }
    }

    public void visit(AssignDesignator assignDesignator) {

    }

    public void visit(DesignatorFactor designatorFactor) {
        Code.load(designatorFactor.obj);
    }

    public void visit(ConstFactor constFactor) {
        // report_info("[ConstFactor] value: " + constFactor.obj.getAdr(), constFactor);
        Code.load(constFactor.obj);
    }

    public void visit(NewOperatorFactor newOperatorFactor) {
        // report_info("[NewOp] " + newOperatorFactor.obj.getType().getNumberOfFields(), newOperatorFactor);
        Code.put(Code.new_);
        Code.put2(newOperatorFactor.obj.getType().getNumberOfFields() * 4);

        // LOAD VTF for newly created object of a class
        Code.put(Code.dup);
        Code.loadConst(vtfPointers.get(newOperatorFactor.getType().struct));
        Code.put(Code.putfield);
        Code.put2(0);
    }

    public void visit(NewOperatorFactorWithBrackets newOperatorFactorWithBrackets) {
        Code.put(Code.newarray);
        if (newOperatorFactorWithBrackets.getType().struct.getKind() == Struct.Char ||
            newOperatorFactorWithBrackets.getType().struct.getKind() == Struct.Bool) {
            Code.put(0);
        }
        else {
            Code.put(1);
        }
    }

    public void visit(SingleDesignator singleDesignator) {
        //Code.load(singleDesignator.obj);
        //report_info("LMAOs pass:" + ejlmao + " " + stringifyObjNode(singleDesignator.obj), singleDesignator);
        if (singleDesignator.obj.getKind() == Obj.Meth) {
            boolean isClassMethod = false;
            Collection<Obj> locals = singleDesignator.obj.getLocalSymbols();
            int formalArgCount = singleDesignator.obj.getLevel();
            if (formalArgCount > 0) {
                isClassMethod = locals.iterator().next().getName().equals("this");
            }
            if (isClassMethod) {
                Code.put(Code.load);
                Code.put(0);
            }
        }
    }

    public void visit(InnerExprInBracketsDesignator innerExprInBracketsDesignator) {
        Code.load(innerExprInBracketsDesignator.getDesignator().obj);
        Code.put(Code.dup_x1);
        Code.put(Code.pop);
        // report_info("LMAOb pass:" + ejlmao + " " + stringifyObjNode(innerExprInBracketsDesignator.obj), innerExprInBracketsDesignator);
    }

    public void visit(InnerDotIdentDesignator innerDotIdentDesignator) {
        Code.load(innerDotIdentDesignator.getDesignator().obj);
        // report_info("LMAOi pass:" + ejlmao + " " + stringifyObjNode(innerDotIdentDesignator.obj), innerDotIdentDesignator);
    }
}

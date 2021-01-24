package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
        createBuiltinFunctions();
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

    public void visit(RegularExpr regularExpr) {}

    public void visit(SingleTermExpr singleTermExpr) {}

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

    public void visit(SingleTerm singleTerm) {}

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
        report_info("[ConstFactor] value: " + constFactor.obj.getAdr(), constFactor);
        Code.load(constFactor.obj);
    }

    public void visit(NewOperatorFactor newOperatorFactor) {
        report_info("[NewOp] " + newOperatorFactor.obj.getType().getNumberOfFields(), newOperatorFactor);
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

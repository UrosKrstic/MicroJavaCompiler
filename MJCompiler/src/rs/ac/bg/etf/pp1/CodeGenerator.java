package rs.ac.bg.etf.pp1;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.concepts.Struct;

import org.apache.log4j.Logger;

public class CodeGenerator extends VisitorAdaptor {
    public int mainPc;

    private static Logger log = Logger.getLogger(CodeGenerator.class);

    public void report_info(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(", line ").append(line);
        log.info(msg.toString());
    }

    public void visit(MethodReturnTypeAndName methodReturnTypeAndName) {
        methodReturnTypeAndName.obj.setAdr(Code.pc);
        if ("main".equals(methodReturnTypeAndName.getMethodName())) {
            mainPc = Code.pc;
        }
        int formalParamCount = methodReturnTypeAndName.obj.getLevel();
        int localVarCount = methodReturnTypeAndName.obj.getLocalSymbols().size() - formalParamCount;
        report_info("[Method definition] fpcnt: " + formalParamCount + ", lvcnt: " + localVarCount, methodReturnTypeAndName);

        Code.put(Code.enter);
        Code.put(formalParamCount);
        Code.put(formalParamCount + localVarCount);
    }

    public void visit(MethodDecl methodDecl) {
        Code.put(Code.exit); 
        Code.put(Code.return_);
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
    }

    public void visit(InnerExprInBracketsDesignator innerExprInBracketsDesignator) {
        Code.load(innerExprInBracketsDesignator.getDesignator().obj);
        Code.put(Code.dup_x1);
        Code.put(Code.pop);
    }

    public void visit(InnerDotIdentDesignator innerDotIdentDesignator) {
        Code.load(innerDotIdentDesignator.getDesignator().obj);

    }
}

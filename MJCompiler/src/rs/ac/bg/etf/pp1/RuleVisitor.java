package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;

public class RuleVisitor extends VisitorAdaptor {

    int constDeclListCount = 0, programCallCount;
    int constDeclCallCount = 0, firstConstDeclCallCount = 0;

    public void visit(ConstDeclList ConstDeclList) {
        constDeclListCount++;
    }

    public void visit(Program Program) {
        programCallCount++;
    }

    public void visit(ConstDecl ConstDecl) {
        constDeclCallCount++;
    }
}

// generated with ast extension for cup
// version 0.8
// 15/11/2020 15:34:27


package rs.ac.bg.etf.pp1.ast;

public class IfElseStatement extends Statement {

    private ConditionExpr ConditionExpr;
    private Statement Statement;
    private OptionalElseStatement OptionalElseStatement;

    public IfElseStatement (ConditionExpr ConditionExpr, Statement Statement, OptionalElseStatement OptionalElseStatement) {
        this.ConditionExpr=ConditionExpr;
        if(ConditionExpr!=null) ConditionExpr.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.OptionalElseStatement=OptionalElseStatement;
        if(OptionalElseStatement!=null) OptionalElseStatement.setParent(this);
    }

    public ConditionExpr getConditionExpr() {
        return ConditionExpr;
    }

    public void setConditionExpr(ConditionExpr ConditionExpr) {
        this.ConditionExpr=ConditionExpr;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public OptionalElseStatement getOptionalElseStatement() {
        return OptionalElseStatement;
    }

    public void setOptionalElseStatement(OptionalElseStatement OptionalElseStatement) {
        this.OptionalElseStatement=OptionalElseStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConditionExpr!=null) ConditionExpr.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(OptionalElseStatement!=null) OptionalElseStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConditionExpr!=null) ConditionExpr.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(OptionalElseStatement!=null) OptionalElseStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConditionExpr!=null) ConditionExpr.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(OptionalElseStatement!=null) OptionalElseStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfElseStatement(\n");

        if(ConditionExpr!=null)
            buffer.append(ConditionExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalElseStatement!=null)
            buffer.append(OptionalElseStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IfElseStatement]");
        return buffer.toString();
    }
}

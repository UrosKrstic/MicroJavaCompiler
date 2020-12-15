// generated with ast extension for cup
// version 0.8
// 15/11/2020 10:32:40


package rs.ac.bg.etf.pp1.ast;

public class DoWhileStatement extends Statement {

    private Statement Statement;
    private ConditionExpr ConditionExpr;

    public DoWhileStatement (Statement Statement, ConditionExpr ConditionExpr) {
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.ConditionExpr=ConditionExpr;
        if(ConditionExpr!=null) ConditionExpr.setParent(this);
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public ConditionExpr getConditionExpr() {
        return ConditionExpr;
    }

    public void setConditionExpr(ConditionExpr ConditionExpr) {
        this.ConditionExpr=ConditionExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Statement!=null) Statement.accept(visitor);
        if(ConditionExpr!=null) ConditionExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(ConditionExpr!=null) ConditionExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(ConditionExpr!=null) ConditionExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DoWhileStatement(\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConditionExpr!=null)
            buffer.append(ConditionExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DoWhileStatement]");
        return buffer.toString();
    }
}

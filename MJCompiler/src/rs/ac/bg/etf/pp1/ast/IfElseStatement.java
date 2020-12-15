// generated with ast extension for cup
// version 0.8
// 16/11/2020 0:27:20


package rs.ac.bg.etf.pp1.ast;

public class IfElseStatement extends Statement {

    private IfCondExpr IfCondExpr;
    private Statement Statement;
    private OptionalElseStatement OptionalElseStatement;

    public IfElseStatement (IfCondExpr IfCondExpr, Statement Statement, OptionalElseStatement OptionalElseStatement) {
        this.IfCondExpr=IfCondExpr;
        if(IfCondExpr!=null) IfCondExpr.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.OptionalElseStatement=OptionalElseStatement;
        if(OptionalElseStatement!=null) OptionalElseStatement.setParent(this);
    }

    public IfCondExpr getIfCondExpr() {
        return IfCondExpr;
    }

    public void setIfCondExpr(IfCondExpr IfCondExpr) {
        this.IfCondExpr=IfCondExpr;
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
        if(IfCondExpr!=null) IfCondExpr.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(OptionalElseStatement!=null) OptionalElseStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfCondExpr!=null) IfCondExpr.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(OptionalElseStatement!=null) OptionalElseStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfCondExpr!=null) IfCondExpr.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(OptionalElseStatement!=null) OptionalElseStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfElseStatement(\n");

        if(IfCondExpr!=null)
            buffer.append(IfCondExpr.toString("  "+tab));
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

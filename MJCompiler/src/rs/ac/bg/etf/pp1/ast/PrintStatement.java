// generated with ast extension for cup
// version 0.8
// 15/11/2020 10:32:40


package rs.ac.bg.etf.pp1.ast;

public class PrintStatement extends Statement {

    private FullPrintStatement FullPrintStatement;

    public PrintStatement (FullPrintStatement FullPrintStatement) {
        this.FullPrintStatement=FullPrintStatement;
        if(FullPrintStatement!=null) FullPrintStatement.setParent(this);
    }

    public FullPrintStatement getFullPrintStatement() {
        return FullPrintStatement;
    }

    public void setFullPrintStatement(FullPrintStatement FullPrintStatement) {
        this.FullPrintStatement=FullPrintStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FullPrintStatement!=null) FullPrintStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FullPrintStatement!=null) FullPrintStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FullPrintStatement!=null) FullPrintStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("PrintStatement(\n");

        if(FullPrintStatement!=null)
            buffer.append(FullPrintStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [PrintStatement]");
        return buffer.toString();
    }
}

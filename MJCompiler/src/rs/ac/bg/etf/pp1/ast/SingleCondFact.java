// generated with ast extension for cup
// version 0.8
// 15/11/2020 10:32:40


package rs.ac.bg.etf.pp1.ast;

public class SingleCondFact extends CondFact {

    private TermExpr TermExpr;

    public SingleCondFact (TermExpr TermExpr) {
        this.TermExpr=TermExpr;
        if(TermExpr!=null) TermExpr.setParent(this);
    }

    public TermExpr getTermExpr() {
        return TermExpr;
    }

    public void setTermExpr(TermExpr TermExpr) {
        this.TermExpr=TermExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(TermExpr!=null) TermExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(TermExpr!=null) TermExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(TermExpr!=null) TermExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleCondFact(\n");

        if(TermExpr!=null)
            buffer.append(TermExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleCondFact]");
        return buffer.toString();
    }
}

// generated with ast extension for cup
// version 0.8
// 15/11/2020 15:34:27


package rs.ac.bg.etf.pp1.ast;

public class FullCondFact extends CondFact {

    private TermExpr TermExpr;
    private Relop Relop;
    private TermExpr TermExpr1;

    public FullCondFact (TermExpr TermExpr, Relop Relop, TermExpr TermExpr1) {
        this.TermExpr=TermExpr;
        if(TermExpr!=null) TermExpr.setParent(this);
        this.Relop=Relop;
        if(Relop!=null) Relop.setParent(this);
        this.TermExpr1=TermExpr1;
        if(TermExpr1!=null) TermExpr1.setParent(this);
    }

    public TermExpr getTermExpr() {
        return TermExpr;
    }

    public void setTermExpr(TermExpr TermExpr) {
        this.TermExpr=TermExpr;
    }

    public Relop getRelop() {
        return Relop;
    }

    public void setRelop(Relop Relop) {
        this.Relop=Relop;
    }

    public TermExpr getTermExpr1() {
        return TermExpr1;
    }

    public void setTermExpr1(TermExpr TermExpr1) {
        this.TermExpr1=TermExpr1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(TermExpr!=null) TermExpr.accept(visitor);
        if(Relop!=null) Relop.accept(visitor);
        if(TermExpr1!=null) TermExpr1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(TermExpr!=null) TermExpr.traverseTopDown(visitor);
        if(Relop!=null) Relop.traverseTopDown(visitor);
        if(TermExpr1!=null) TermExpr1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(TermExpr!=null) TermExpr.traverseBottomUp(visitor);
        if(Relop!=null) Relop.traverseBottomUp(visitor);
        if(TermExpr1!=null) TermExpr1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FullCondFact(\n");

        if(TermExpr!=null)
            buffer.append(TermExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Relop!=null)
            buffer.append(Relop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(TermExpr1!=null)
            buffer.append(TermExpr1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FullCondFact]");
        return buffer.toString();
    }
}

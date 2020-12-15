// generated with ast extension for cup
// version 0.8
// 16/11/2020 0:27:20


package rs.ac.bg.etf.pp1.ast;

public class RegularExpr extends Expr {

    private TermExpr TermExpr;

    public RegularExpr (TermExpr TermExpr) {
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
        buffer.append("RegularExpr(\n");

        if(TermExpr!=null)
            buffer.append(TermExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [RegularExpr]");
        return buffer.toString();
    }
}

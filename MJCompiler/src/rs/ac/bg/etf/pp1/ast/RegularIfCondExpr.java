// generated with ast extension for cup
// version 0.8
// 16/11/2020 0:27:20


package rs.ac.bg.etf.pp1.ast;

public class RegularIfCondExpr extends IfCondExpr {

    private ConditionExpr ConditionExpr;

    public RegularIfCondExpr (ConditionExpr ConditionExpr) {
        this.ConditionExpr=ConditionExpr;
        if(ConditionExpr!=null) ConditionExpr.setParent(this);
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
        if(ConditionExpr!=null) ConditionExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConditionExpr!=null) ConditionExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConditionExpr!=null) ConditionExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("RegularIfCondExpr(\n");

        if(ConditionExpr!=null)
            buffer.append(ConditionExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [RegularIfCondExpr]");
        return buffer.toString();
    }
}

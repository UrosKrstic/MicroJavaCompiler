// generated with ast extension for cup
// version 0.8
// 15/11/2020 10:32:40


package rs.ac.bg.etf.pp1.ast;

public class TernaryConditionExpr extends ConditionExpr {

    private Condition Condition;
    private ConditionExpr ConditionExpr;
    private ConditionExpr ConditionExpr1;

    public TernaryConditionExpr (Condition Condition, ConditionExpr ConditionExpr, ConditionExpr ConditionExpr1) {
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.ConditionExpr=ConditionExpr;
        if(ConditionExpr!=null) ConditionExpr.setParent(this);
        this.ConditionExpr1=ConditionExpr1;
        if(ConditionExpr1!=null) ConditionExpr1.setParent(this);
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public ConditionExpr getConditionExpr() {
        return ConditionExpr;
    }

    public void setConditionExpr(ConditionExpr ConditionExpr) {
        this.ConditionExpr=ConditionExpr;
    }

    public ConditionExpr getConditionExpr1() {
        return ConditionExpr1;
    }

    public void setConditionExpr1(ConditionExpr ConditionExpr1) {
        this.ConditionExpr1=ConditionExpr1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Condition!=null) Condition.accept(visitor);
        if(ConditionExpr!=null) ConditionExpr.accept(visitor);
        if(ConditionExpr1!=null) ConditionExpr1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(ConditionExpr!=null) ConditionExpr.traverseTopDown(visitor);
        if(ConditionExpr1!=null) ConditionExpr1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(ConditionExpr!=null) ConditionExpr.traverseBottomUp(visitor);
        if(ConditionExpr1!=null) ConditionExpr1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("TernaryConditionExpr(\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConditionExpr!=null)
            buffer.append(ConditionExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConditionExpr1!=null)
            buffer.append(ConditionExpr1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [TernaryConditionExpr]");
        return buffer.toString();
    }
}

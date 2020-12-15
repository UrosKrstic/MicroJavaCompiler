// generated with ast extension for cup
// version 0.8
// 15/11/2020 15:34:27


package rs.ac.bg.etf.pp1.ast;

public class DesignatorFactor extends Factor {

    private Designator Designator;
    private OptionalActParsWithBrackets OptionalActParsWithBrackets;

    public DesignatorFactor (Designator Designator, OptionalActParsWithBrackets OptionalActParsWithBrackets) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.OptionalActParsWithBrackets=OptionalActParsWithBrackets;
        if(OptionalActParsWithBrackets!=null) OptionalActParsWithBrackets.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public OptionalActParsWithBrackets getOptionalActParsWithBrackets() {
        return OptionalActParsWithBrackets;
    }

    public void setOptionalActParsWithBrackets(OptionalActParsWithBrackets OptionalActParsWithBrackets) {
        this.OptionalActParsWithBrackets=OptionalActParsWithBrackets;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(OptionalActParsWithBrackets!=null) OptionalActParsWithBrackets.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(OptionalActParsWithBrackets!=null) OptionalActParsWithBrackets.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(OptionalActParsWithBrackets!=null) OptionalActParsWithBrackets.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorFactor(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalActParsWithBrackets!=null)
            buffer.append(OptionalActParsWithBrackets.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorFactor]");
        return buffer.toString();
    }
}

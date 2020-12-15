// generated with ast extension for cup
// version 0.8
// 15/11/2020 10:32:40


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatement extends Statement {

    private Designator Designator;
    private DesignatorStatementOptions DesignatorStatementOptions;

    public DesignatorStatement (Designator Designator, DesignatorStatementOptions DesignatorStatementOptions) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DesignatorStatementOptions=DesignatorStatementOptions;
        if(DesignatorStatementOptions!=null) DesignatorStatementOptions.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public DesignatorStatementOptions getDesignatorStatementOptions() {
        return DesignatorStatementOptions;
    }

    public void setDesignatorStatementOptions(DesignatorStatementOptions DesignatorStatementOptions) {
        this.DesignatorStatementOptions=DesignatorStatementOptions;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(DesignatorStatementOptions!=null) DesignatorStatementOptions.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DesignatorStatementOptions!=null) DesignatorStatementOptions.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DesignatorStatementOptions!=null) DesignatorStatementOptions.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatement(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatementOptions!=null)
            buffer.append(DesignatorStatementOptions.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatement]");
        return buffer.toString();
    }
}

// generated with ast extension for cup
// version 0.8
// 15/11/2020 10:32:40


package rs.ac.bg.etf.pp1.ast;

public class FunctionCall extends DesignatorStatementOptions {

    private OptionalActPars OptionalActPars;

    public FunctionCall (OptionalActPars OptionalActPars) {
        this.OptionalActPars=OptionalActPars;
        if(OptionalActPars!=null) OptionalActPars.setParent(this);
    }

    public OptionalActPars getOptionalActPars() {
        return OptionalActPars;
    }

    public void setOptionalActPars(OptionalActPars OptionalActPars) {
        this.OptionalActPars=OptionalActPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OptionalActPars!=null) OptionalActPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptionalActPars!=null) OptionalActPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptionalActPars!=null) OptionalActPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FunctionCall(\n");

        if(OptionalActPars!=null)
            buffer.append(OptionalActPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FunctionCall]");
        return buffer.toString();
    }
}

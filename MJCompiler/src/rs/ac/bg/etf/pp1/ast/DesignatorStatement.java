// generated with ast extension for cup
// version 0.8
// 16/11/2020 0:27:20


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatement extends Statement {

    private DesignatorStatementOptions DesignatorStatementOptions;

    public DesignatorStatement (DesignatorStatementOptions DesignatorStatementOptions) {
        this.DesignatorStatementOptions=DesignatorStatementOptions;
        if(DesignatorStatementOptions!=null) DesignatorStatementOptions.setParent(this);
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
        if(DesignatorStatementOptions!=null) DesignatorStatementOptions.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorStatementOptions!=null) DesignatorStatementOptions.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorStatementOptions!=null) DesignatorStatementOptions.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatement(\n");

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

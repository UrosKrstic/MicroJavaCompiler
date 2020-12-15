// generated with ast extension for cup
// version 0.8
// 16/11/2020 0:27:20


package rs.ac.bg.etf.pp1.ast;

public class AssignStatementWithErrRecovery extends DesignatorStatementOptions {

    private AssignStatementRecover AssignStatementRecover;

    public AssignStatementWithErrRecovery (AssignStatementRecover AssignStatementRecover) {
        this.AssignStatementRecover=AssignStatementRecover;
        if(AssignStatementRecover!=null) AssignStatementRecover.setParent(this);
    }

    public AssignStatementRecover getAssignStatementRecover() {
        return AssignStatementRecover;
    }

    public void setAssignStatementRecover(AssignStatementRecover AssignStatementRecover) {
        this.AssignStatementRecover=AssignStatementRecover;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AssignStatementRecover!=null) AssignStatementRecover.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AssignStatementRecover!=null) AssignStatementRecover.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AssignStatementRecover!=null) AssignStatementRecover.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AssignStatementWithErrRecovery(\n");

        if(AssignStatementRecover!=null)
            buffer.append(AssignStatementRecover.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AssignStatementWithErrRecovery]");
        return buffer.toString();
    }
}

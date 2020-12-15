// generated with ast extension for cup
// version 0.8
// 15/11/2020 10:32:40


package rs.ac.bg.etf.pp1.ast;

public class ConstFactor extends Factor {

    private ConstName ConstName;

    public ConstFactor (ConstName ConstName) {
        this.ConstName=ConstName;
        if(ConstName!=null) ConstName.setParent(this);
    }

    public ConstName getConstName() {
        return ConstName;
    }

    public void setConstName(ConstName ConstName) {
        this.ConstName=ConstName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstName!=null) ConstName.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstName!=null) ConstName.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstName!=null) ConstName.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstFactor(\n");

        if(ConstName!=null)
            buffer.append(ConstName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstFactor]");
        return buffer.toString();
    }
}

// generated with ast extension for cup
// version 0.8
// 16/11/2020 0:27:20


package rs.ac.bg.etf.pp1.ast;

public class SubAddop extends Addop {

    public SubAddop () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SubAddop(\n");

        buffer.append(tab);
        buffer.append(") [SubAddop]");
        return buffer.toString();
    }
}

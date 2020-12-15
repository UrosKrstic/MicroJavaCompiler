// generated with ast extension for cup
// version 0.8
// 16/11/2020 0:27:20


package rs.ac.bg.etf.pp1.ast;

public class SingleVarDecl extends GlobalVarDecl {

    private String I1;
    private OptionalArrayBrackets OptionalArrayBrackets;

    public SingleVarDecl (String I1, OptionalArrayBrackets OptionalArrayBrackets) {
        this.I1=I1;
        this.OptionalArrayBrackets=OptionalArrayBrackets;
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public OptionalArrayBrackets getOptionalArrayBrackets() {
        return OptionalArrayBrackets;
    }

    public void setOptionalArrayBrackets(OptionalArrayBrackets OptionalArrayBrackets) {
        this.OptionalArrayBrackets=OptionalArrayBrackets;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleVarDecl(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(OptionalArrayBrackets!=null)
            buffer.append(OptionalArrayBrackets.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleVarDecl]");
        return buffer.toString();
    }
}

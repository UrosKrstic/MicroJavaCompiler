// generated with ast extension for cup
// version 0.8
// 16/11/2020 0:27:20


package rs.ac.bg.etf.pp1.ast;

public class NewOperatorFactor extends Factor {

    private Type Type;
    private OptionalExprInBrackets OptionalExprInBrackets;

    public NewOperatorFactor (Type Type, OptionalExprInBrackets OptionalExprInBrackets) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.OptionalExprInBrackets=OptionalExprInBrackets;
        if(OptionalExprInBrackets!=null) OptionalExprInBrackets.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public OptionalExprInBrackets getOptionalExprInBrackets() {
        return OptionalExprInBrackets;
    }

    public void setOptionalExprInBrackets(OptionalExprInBrackets OptionalExprInBrackets) {
        this.OptionalExprInBrackets=OptionalExprInBrackets;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(OptionalExprInBrackets!=null) OptionalExprInBrackets.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(OptionalExprInBrackets!=null) OptionalExprInBrackets.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(OptionalExprInBrackets!=null) OptionalExprInBrackets.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NewOperatorFactor(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalExprInBrackets!=null)
            buffer.append(OptionalExprInBrackets.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NewOperatorFactor]");
        return buffer.toString();
    }
}

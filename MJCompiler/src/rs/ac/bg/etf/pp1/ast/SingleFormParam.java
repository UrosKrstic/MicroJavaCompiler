// generated with ast extension for cup
// version 0.8
// 15/11/2020 10:32:40


package rs.ac.bg.etf.pp1.ast;

public class SingleFormParam extends FormsPars {

    private Type Type;
    private String I2;
    private OptionalArrayBrackets OptionalArrayBrackets;

    public SingleFormParam (Type Type, String I2, OptionalArrayBrackets OptionalArrayBrackets) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.I2=I2;
        this.OptionalArrayBrackets=OptionalArrayBrackets;
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getI2() {
        return I2;
    }

    public void setI2(String I2) {
        this.I2=I2;
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
        if(Type!=null) Type.accept(visitor);
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleFormParam(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        if(OptionalArrayBrackets!=null)
            buffer.append(OptionalArrayBrackets.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleFormParam]");
        return buffer.toString();
    }
}

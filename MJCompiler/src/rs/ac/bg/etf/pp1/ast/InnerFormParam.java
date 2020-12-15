// generated with ast extension for cup
// version 0.8
// 15/11/2020 10:32:40


package rs.ac.bg.etf.pp1.ast;

public class InnerFormParam extends FormsPars {

    private FormsPars FormsPars;
    private Type Type;
    private String I3;
    private OptionalArrayBrackets OptionalArrayBrackets;

    public InnerFormParam (FormsPars FormsPars, Type Type, String I3, OptionalArrayBrackets OptionalArrayBrackets) {
        this.FormsPars=FormsPars;
        if(FormsPars!=null) FormsPars.setParent(this);
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.I3=I3;
        this.OptionalArrayBrackets=OptionalArrayBrackets;
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.setParent(this);
    }

    public FormsPars getFormsPars() {
        return FormsPars;
    }

    public void setFormsPars(FormsPars FormsPars) {
        this.FormsPars=FormsPars;
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getI3() {
        return I3;
    }

    public void setI3(String I3) {
        this.I3=I3;
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
        if(FormsPars!=null) FormsPars.accept(visitor);
        if(Type!=null) Type.accept(visitor);
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormsPars!=null) FormsPars.traverseTopDown(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormsPars!=null) FormsPars.traverseBottomUp(visitor);
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("InnerFormParam(\n");

        if(FormsPars!=null)
            buffer.append(FormsPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I3);
        buffer.append("\n");

        if(OptionalArrayBrackets!=null)
            buffer.append(OptionalArrayBrackets.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InnerFormParam]");
        return buffer.toString();
    }
}

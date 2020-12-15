// generated with ast extension for cup
// version 0.8
// 15/11/2020 15:34:27


package rs.ac.bg.etf.pp1.ast;

public class NextVarDecl extends VarDecl {

    private VarDecl VarDecl;
    private String I2;
    private OptionalArrayBrackets OptionalArrayBrackets;

    public NextVarDecl (VarDecl VarDecl, String I2, OptionalArrayBrackets OptionalArrayBrackets) {
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
        this.I2=I2;
        this.OptionalArrayBrackets=OptionalArrayBrackets;
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.setParent(this);
    }

    public VarDecl getVarDecl() {
        return VarDecl;
    }

    public void setVarDecl(VarDecl VarDecl) {
        this.VarDecl=VarDecl;
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
        if(VarDecl!=null) VarDecl.accept(visitor);
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NextVarDecl(\n");

        if(VarDecl!=null)
            buffer.append(VarDecl.toString("  "+tab));
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
        buffer.append(") [NextVarDecl]");
        return buffer.toString();
    }
}

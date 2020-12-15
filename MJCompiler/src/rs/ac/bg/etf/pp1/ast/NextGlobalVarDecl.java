// generated with ast extension for cup
// version 0.8
// 16/11/2020 0:27:20


package rs.ac.bg.etf.pp1.ast;

public class NextGlobalVarDecl extends GlobalVarDecl {

    private GlobalVarDecl GlobalVarDecl;
    private String I2;
    private OptionalArrayBrackets OptionalArrayBrackets;

    public NextGlobalVarDecl (GlobalVarDecl GlobalVarDecl, String I2, OptionalArrayBrackets OptionalArrayBrackets) {
        this.GlobalVarDecl=GlobalVarDecl;
        if(GlobalVarDecl!=null) GlobalVarDecl.setParent(this);
        this.I2=I2;
        this.OptionalArrayBrackets=OptionalArrayBrackets;
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.setParent(this);
    }

    public GlobalVarDecl getGlobalVarDecl() {
        return GlobalVarDecl;
    }

    public void setGlobalVarDecl(GlobalVarDecl GlobalVarDecl) {
        this.GlobalVarDecl=GlobalVarDecl;
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
        if(GlobalVarDecl!=null) GlobalVarDecl.accept(visitor);
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(GlobalVarDecl!=null) GlobalVarDecl.traverseTopDown(visitor);
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(GlobalVarDecl!=null) GlobalVarDecl.traverseBottomUp(visitor);
        if(OptionalArrayBrackets!=null) OptionalArrayBrackets.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NextGlobalVarDecl(\n");

        if(GlobalVarDecl!=null)
            buffer.append(GlobalVarDecl.toString("  "+tab));
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
        buffer.append(") [NextGlobalVarDecl]");
        return buffer.toString();
    }
}

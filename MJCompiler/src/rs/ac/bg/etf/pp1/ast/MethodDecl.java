// generated with ast extension for cup
// version 0.8
// 15/11/2020 10:32:40


package rs.ac.bg.etf.pp1.ast;

public class MethodDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private VoidOrType VoidOrType;
    private String I2;
    private OptionalFormPars OptionalFormPars;
    private OptionalVarDeclList OptionalVarDeclList;
    private StatementList StatementList;

    public MethodDecl (VoidOrType VoidOrType, String I2, OptionalFormPars OptionalFormPars, OptionalVarDeclList OptionalVarDeclList, StatementList StatementList) {
        this.VoidOrType=VoidOrType;
        if(VoidOrType!=null) VoidOrType.setParent(this);
        this.I2=I2;
        this.OptionalFormPars=OptionalFormPars;
        if(OptionalFormPars!=null) OptionalFormPars.setParent(this);
        this.OptionalVarDeclList=OptionalVarDeclList;
        if(OptionalVarDeclList!=null) OptionalVarDeclList.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public VoidOrType getVoidOrType() {
        return VoidOrType;
    }

    public void setVoidOrType(VoidOrType VoidOrType) {
        this.VoidOrType=VoidOrType;
    }

    public String getI2() {
        return I2;
    }

    public void setI2(String I2) {
        this.I2=I2;
    }

    public OptionalFormPars getOptionalFormPars() {
        return OptionalFormPars;
    }

    public void setOptionalFormPars(OptionalFormPars OptionalFormPars) {
        this.OptionalFormPars=OptionalFormPars;
    }

    public OptionalVarDeclList getOptionalVarDeclList() {
        return OptionalVarDeclList;
    }

    public void setOptionalVarDeclList(OptionalVarDeclList OptionalVarDeclList) {
        this.OptionalVarDeclList=OptionalVarDeclList;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VoidOrType!=null) VoidOrType.accept(visitor);
        if(OptionalFormPars!=null) OptionalFormPars.accept(visitor);
        if(OptionalVarDeclList!=null) OptionalVarDeclList.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VoidOrType!=null) VoidOrType.traverseTopDown(visitor);
        if(OptionalFormPars!=null) OptionalFormPars.traverseTopDown(visitor);
        if(OptionalVarDeclList!=null) OptionalVarDeclList.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VoidOrType!=null) VoidOrType.traverseBottomUp(visitor);
        if(OptionalFormPars!=null) OptionalFormPars.traverseBottomUp(visitor);
        if(OptionalVarDeclList!=null) OptionalVarDeclList.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDecl(\n");

        if(VoidOrType!=null)
            buffer.append(VoidOrType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        if(OptionalFormPars!=null)
            buffer.append(OptionalFormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalVarDeclList!=null)
            buffer.append(OptionalVarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDecl]");
        return buffer.toString();
    }
}

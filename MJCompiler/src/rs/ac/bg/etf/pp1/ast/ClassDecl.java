// generated with ast extension for cup
// version 0.8
// 15/11/2020 15:34:27


package rs.ac.bg.etf.pp1.ast;

public class ClassDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String I1;
    private OptionalExtendedClass OptionalExtendedClass;
    private OptionalVarDeclList OptionalVarDeclList;
    private MethodDeclListWithBraces MethodDeclListWithBraces;

    public ClassDecl (String I1, OptionalExtendedClass OptionalExtendedClass, OptionalVarDeclList OptionalVarDeclList, MethodDeclListWithBraces MethodDeclListWithBraces) {
        this.I1=I1;
        this.OptionalExtendedClass=OptionalExtendedClass;
        if(OptionalExtendedClass!=null) OptionalExtendedClass.setParent(this);
        this.OptionalVarDeclList=OptionalVarDeclList;
        if(OptionalVarDeclList!=null) OptionalVarDeclList.setParent(this);
        this.MethodDeclListWithBraces=MethodDeclListWithBraces;
        if(MethodDeclListWithBraces!=null) MethodDeclListWithBraces.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public OptionalExtendedClass getOptionalExtendedClass() {
        return OptionalExtendedClass;
    }

    public void setOptionalExtendedClass(OptionalExtendedClass OptionalExtendedClass) {
        this.OptionalExtendedClass=OptionalExtendedClass;
    }

    public OptionalVarDeclList getOptionalVarDeclList() {
        return OptionalVarDeclList;
    }

    public void setOptionalVarDeclList(OptionalVarDeclList OptionalVarDeclList) {
        this.OptionalVarDeclList=OptionalVarDeclList;
    }

    public MethodDeclListWithBraces getMethodDeclListWithBraces() {
        return MethodDeclListWithBraces;
    }

    public void setMethodDeclListWithBraces(MethodDeclListWithBraces MethodDeclListWithBraces) {
        this.MethodDeclListWithBraces=MethodDeclListWithBraces;
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
        if(OptionalExtendedClass!=null) OptionalExtendedClass.accept(visitor);
        if(OptionalVarDeclList!=null) OptionalVarDeclList.accept(visitor);
        if(MethodDeclListWithBraces!=null) MethodDeclListWithBraces.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptionalExtendedClass!=null) OptionalExtendedClass.traverseTopDown(visitor);
        if(OptionalVarDeclList!=null) OptionalVarDeclList.traverseTopDown(visitor);
        if(MethodDeclListWithBraces!=null) MethodDeclListWithBraces.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptionalExtendedClass!=null) OptionalExtendedClass.traverseBottomUp(visitor);
        if(OptionalVarDeclList!=null) OptionalVarDeclList.traverseBottomUp(visitor);
        if(MethodDeclListWithBraces!=null) MethodDeclListWithBraces.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDecl(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(OptionalExtendedClass!=null)
            buffer.append(OptionalExtendedClass.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalVarDeclList!=null)
            buffer.append(OptionalVarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDeclListWithBraces!=null)
            buffer.append(MethodDeclListWithBraces.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDecl]");
        return buffer.toString();
    }
}

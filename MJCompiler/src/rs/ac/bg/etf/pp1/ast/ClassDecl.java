// generated with ast extension for cup
// version 0.8
// 16/11/2020 0:27:20


package rs.ac.bg.etf.pp1.ast;

public class ClassDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String I1;
    private OptionalExtendedClassLBrace OptionalExtendedClassLBrace;
    private ClassOptionalVarDeclList ClassOptionalVarDeclList;
    private MethodDeclListWithBraces MethodDeclListWithBraces;

    public ClassDecl (String I1, OptionalExtendedClassLBrace OptionalExtendedClassLBrace, ClassOptionalVarDeclList ClassOptionalVarDeclList, MethodDeclListWithBraces MethodDeclListWithBraces) {
        this.I1=I1;
        this.OptionalExtendedClassLBrace=OptionalExtendedClassLBrace;
        if(OptionalExtendedClassLBrace!=null) OptionalExtendedClassLBrace.setParent(this);
        this.ClassOptionalVarDeclList=ClassOptionalVarDeclList;
        if(ClassOptionalVarDeclList!=null) ClassOptionalVarDeclList.setParent(this);
        this.MethodDeclListWithBraces=MethodDeclListWithBraces;
        if(MethodDeclListWithBraces!=null) MethodDeclListWithBraces.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public OptionalExtendedClassLBrace getOptionalExtendedClassLBrace() {
        return OptionalExtendedClassLBrace;
    }

    public void setOptionalExtendedClassLBrace(OptionalExtendedClassLBrace OptionalExtendedClassLBrace) {
        this.OptionalExtendedClassLBrace=OptionalExtendedClassLBrace;
    }

    public ClassOptionalVarDeclList getClassOptionalVarDeclList() {
        return ClassOptionalVarDeclList;
    }

    public void setClassOptionalVarDeclList(ClassOptionalVarDeclList ClassOptionalVarDeclList) {
        this.ClassOptionalVarDeclList=ClassOptionalVarDeclList;
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
        if(OptionalExtendedClassLBrace!=null) OptionalExtendedClassLBrace.accept(visitor);
        if(ClassOptionalVarDeclList!=null) ClassOptionalVarDeclList.accept(visitor);
        if(MethodDeclListWithBraces!=null) MethodDeclListWithBraces.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptionalExtendedClassLBrace!=null) OptionalExtendedClassLBrace.traverseTopDown(visitor);
        if(ClassOptionalVarDeclList!=null) ClassOptionalVarDeclList.traverseTopDown(visitor);
        if(MethodDeclListWithBraces!=null) MethodDeclListWithBraces.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptionalExtendedClassLBrace!=null) OptionalExtendedClassLBrace.traverseBottomUp(visitor);
        if(ClassOptionalVarDeclList!=null) ClassOptionalVarDeclList.traverseBottomUp(visitor);
        if(MethodDeclListWithBraces!=null) MethodDeclListWithBraces.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDecl(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(OptionalExtendedClassLBrace!=null)
            buffer.append(OptionalExtendedClassLBrace.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassOptionalVarDeclList!=null)
            buffer.append(ClassOptionalVarDeclList.toString("  "+tab));
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

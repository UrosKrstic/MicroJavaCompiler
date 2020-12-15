// generated with ast extension for cup
// version 0.8
// 16/11/2020 0:27:20


package rs.ac.bg.etf.pp1.ast;

public class FullClassVarDecl extends ClassOptionalVarDeclList {

    private ClassOptionalVarDeclList ClassOptionalVarDeclList;
    private ClassVarDecls ClassVarDecls;

    public FullClassVarDecl (ClassOptionalVarDeclList ClassOptionalVarDeclList, ClassVarDecls ClassVarDecls) {
        this.ClassOptionalVarDeclList=ClassOptionalVarDeclList;
        if(ClassOptionalVarDeclList!=null) ClassOptionalVarDeclList.setParent(this);
        this.ClassVarDecls=ClassVarDecls;
        if(ClassVarDecls!=null) ClassVarDecls.setParent(this);
    }

    public ClassOptionalVarDeclList getClassOptionalVarDeclList() {
        return ClassOptionalVarDeclList;
    }

    public void setClassOptionalVarDeclList(ClassOptionalVarDeclList ClassOptionalVarDeclList) {
        this.ClassOptionalVarDeclList=ClassOptionalVarDeclList;
    }

    public ClassVarDecls getClassVarDecls() {
        return ClassVarDecls;
    }

    public void setClassVarDecls(ClassVarDecls ClassVarDecls) {
        this.ClassVarDecls=ClassVarDecls;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ClassOptionalVarDeclList!=null) ClassOptionalVarDeclList.accept(visitor);
        if(ClassVarDecls!=null) ClassVarDecls.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassOptionalVarDeclList!=null) ClassOptionalVarDeclList.traverseTopDown(visitor);
        if(ClassVarDecls!=null) ClassVarDecls.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassOptionalVarDeclList!=null) ClassOptionalVarDeclList.traverseBottomUp(visitor);
        if(ClassVarDecls!=null) ClassVarDecls.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FullClassVarDecl(\n");

        if(ClassOptionalVarDeclList!=null)
            buffer.append(ClassOptionalVarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassVarDecls!=null)
            buffer.append(ClassVarDecls.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FullClassVarDecl]");
        return buffer.toString();
    }
}

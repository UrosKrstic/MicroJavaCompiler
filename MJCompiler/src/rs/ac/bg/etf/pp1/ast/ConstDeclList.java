// generated with ast extension for cup
// version 0.8
// 15/11/2020 10:32:40


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclList extends DeclList {

    private DeclList DeclList;
    private ConstDecls ConstDecls;

    public ConstDeclList (DeclList DeclList, ConstDecls ConstDecls) {
        this.DeclList=DeclList;
        if(DeclList!=null) DeclList.setParent(this);
        this.ConstDecls=ConstDecls;
        if(ConstDecls!=null) ConstDecls.setParent(this);
    }

    public DeclList getDeclList() {
        return DeclList;
    }

    public void setDeclList(DeclList DeclList) {
        this.DeclList=DeclList;
    }

    public ConstDecls getConstDecls() {
        return ConstDecls;
    }

    public void setConstDecls(ConstDecls ConstDecls) {
        this.ConstDecls=ConstDecls;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DeclList!=null) DeclList.accept(visitor);
        if(ConstDecls!=null) ConstDecls.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DeclList!=null) DeclList.traverseTopDown(visitor);
        if(ConstDecls!=null) ConstDecls.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DeclList!=null) DeclList.traverseBottomUp(visitor);
        if(ConstDecls!=null) ConstDecls.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclList(\n");

        if(DeclList!=null)
            buffer.append(DeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDecls!=null)
            buffer.append(ConstDecls.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclList]");
        return buffer.toString();
    }
}

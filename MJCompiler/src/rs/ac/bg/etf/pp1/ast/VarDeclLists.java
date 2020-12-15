// generated with ast extension for cup
// version 0.8
// 16/11/2020 0:27:20


package rs.ac.bg.etf.pp1.ast;

public class VarDeclLists extends DeclList {

    private DeclList DeclList;
    private GlobalVarDecls GlobalVarDecls;

    public VarDeclLists (DeclList DeclList, GlobalVarDecls GlobalVarDecls) {
        this.DeclList=DeclList;
        if(DeclList!=null) DeclList.setParent(this);
        this.GlobalVarDecls=GlobalVarDecls;
        if(GlobalVarDecls!=null) GlobalVarDecls.setParent(this);
    }

    public DeclList getDeclList() {
        return DeclList;
    }

    public void setDeclList(DeclList DeclList) {
        this.DeclList=DeclList;
    }

    public GlobalVarDecls getGlobalVarDecls() {
        return GlobalVarDecls;
    }

    public void setGlobalVarDecls(GlobalVarDecls GlobalVarDecls) {
        this.GlobalVarDecls=GlobalVarDecls;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DeclList!=null) DeclList.accept(visitor);
        if(GlobalVarDecls!=null) GlobalVarDecls.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DeclList!=null) DeclList.traverseTopDown(visitor);
        if(GlobalVarDecls!=null) GlobalVarDecls.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DeclList!=null) DeclList.traverseBottomUp(visitor);
        if(GlobalVarDecls!=null) GlobalVarDecls.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclLists(\n");

        if(DeclList!=null)
            buffer.append(DeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(GlobalVarDecls!=null)
            buffer.append(GlobalVarDecls.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclLists]");
        return buffer.toString();
    }
}

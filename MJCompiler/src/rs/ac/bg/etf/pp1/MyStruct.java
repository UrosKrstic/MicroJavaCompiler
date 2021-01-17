package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.structure.SymbolDataStructure;

public class MyStruct extends Struct {

    private Obj objNode;

	public MyStruct(int kind, SymbolDataStructure members) {
		super(kind, members);
	}

	public MyStruct(int kind) {
		super(kind);
	}

	public MyStruct(int kind, Struct elemType) {
		super(kind, elemType);
	}

    public void setObjNode(Obj objNode) {
        this.objNode = objNode;
    }

    public boolean equals(Struct other) {
        if (this.getKind() == Struct.Class && other.getKind() == Struct.Class) {
            return this.objNode.getName().equals(((MyStruct)other).objNode.getName());
        }

		return super.equals(other);
	}
}

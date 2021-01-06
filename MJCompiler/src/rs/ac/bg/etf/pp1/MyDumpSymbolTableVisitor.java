package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;

public class MyDumpSymbolTableVisitor extends DumpSymbolTableVisitor {

    @Override
    public void visitStructNode(Struct structToVisit) {
        switch (structToVisit.getKind()) {
            case Struct.Bool:
                output.append("bool");
                break;
            case Struct.Array:
                if (structToVisit.getElemType().getKind() == Struct.Bool) {
                    output.append("Arr of ");
                    output.append("bool");
                }
                else {
                    super.visitStructNode(structToVisit);
                }
                break;
            default:
                super.visitStructNode(structToVisit);
                break;
        }
    }
}

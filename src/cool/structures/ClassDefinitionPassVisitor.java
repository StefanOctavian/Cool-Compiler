package cool.structures;

import java.util.Set;

import cool.parser.CoolASTBaseRecursiveVisitor;
import cool.parser.CoolASTNode.CoolClass;
import cool.structures.Symbol.ClassSymbol;

public class ClassDefinitionPassVisitor extends CoolASTBaseRecursiveVisitor<Void> {
    @Override
    public Void visitClass(CoolClass cclass) {
        ClassSymbol classSymbol = new ClassSymbol(cclass.name);

        if (cclass.name.equals("SELF_TYPE"))
            SymbolTable.error(cclass.getContext(), cclass.getNameToken(), 
                "Class has illegal name SELF_TYPE");

        if (!SymbolTable.globals.add(classSymbol))
            SymbolTable.error(cclass.getContext(), cclass.getNameToken(), 
                String.format("Class %s is redefined", cclass.name));

        Set<String> illegalParentNames = Set.of("Int", "String", "Bool", "SELF_TYPE");
        if (illegalParentNames.contains(cclass.parentClass != null ? cclass.parentClass : ""))
            SymbolTable.error(cclass.getContext(), cclass.getParentClassToken(),
                "Class " + cclass.name + " has illegal parent " + cclass.parentClass);

        cclass.setSymbol(classSymbol);
        return null;
    }
}

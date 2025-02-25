package cool.structures;

import java.util.ArrayList;
import java.util.List;

import cool.parser.CoolASTBaseRecursiveVisitor;
import cool.parser.CoolASTNode.CoolClass;
import cool.parser.CoolASTNode.CoolProgram;
import cool.structures.Symbol.ClassSymbol;
import cool.structures.Scope.ClassScope;

public class HierarchyResolutionPassVisitor extends CoolASTBaseRecursiveVisitor<Void> {
    private int order = 0;
    public final List<ClassSymbol> classes = new ArrayList<>();

    @Override
    public Void visitProgram(CoolProgram program) {
        super.visitProgram(program);
        
        for (var cls : program.classes) 
            checkCycle(cls);

        addOrdering(SymbolTable.Object);
        return null;
    }

    @Override
    public Void visitClass(CoolClass cclass) {
        ClassSymbol classSymbol = cclass.getSymbol();
        ClassScope classScope = classSymbol.getScope();

        if (cclass.parentClass == null) {
            classScope.setParent(SymbolTable.Object.getScope());
            cclass.setParentSymbol(null);
            return null;
        }

        ClassSymbol parentSymbol = (ClassSymbol)SymbolTable.globals.lookup(cclass.parentClass);
        if (parentSymbol == null) {
            SymbolTable.error(cclass.getContext(), cclass.getParentClassToken(),
                "Class " + cclass.name + " has undefined parent " + cclass.parentClass);
            return null;
        }
        classScope.setParent(parentSymbol.getScope());
        cclass.setParentSymbol(parentSymbol);
        return null;
    }

    private void checkCycle(CoolClass cclass) {
        ClassSymbol classSymbol = cclass.getSymbol();
        ClassScope classScope = classSymbol.getScope();

        Scope ancestor = classScope;
        while (ancestor != null && ancestor != SymbolTable.globals) {
            ancestor = ancestor.getParent();
            if (ancestor == classScope) {
                SymbolTable.error(cclass.getContext(), cclass.getNameToken(),
                    "Inheritance cycle for class " + ((ClassScope)ancestor).classSymbol.getName());
                break;
            }
        }
    }

    private void addOrdering(ClassSymbol node) {
        node.setOrder(order++);
        classes.add(node);
        for (var child : node.children)
            addOrdering(child);
    }
}

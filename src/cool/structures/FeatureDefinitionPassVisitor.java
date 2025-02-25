package cool.structures;

import cool.parser.CoolASTBaseRecursiveVisitor;
import cool.parser.CoolASTNode.CoolAttribute;
import cool.parser.CoolASTNode.CoolClass;
import cool.parser.CoolASTNode.CoolFormalArg;
import cool.parser.CoolASTNode.CoolMethod;
import cool.parser.CoolASTNode.CoolProgram;
import cool.structures.Symbol.ClassSymbol;
import cool.structures.Symbol.VarSymbol;
import cool.structures.Symbol.AttrSymbol;
import cool.structures.Symbol.ArgSymbol;
import cool.structures.Symbol.MethodSymbol;
import cool.structures.Scope.ClassScope;
import cool.structures.Scope.MethodScope;

public class FeatureDefinitionPassVisitor extends CoolASTBaseRecursiveVisitor<Void> {
    ClassScope classScope;
    MethodScope methodScope;
    int formalIndex = 0;

    @Override
    public Void visitProgram(CoolProgram program) {
        for (var cls : program.classes) {
            classScope = cls.getSymbol().getScope();
            visitClass(cls);
        }
        return null;
    }

    @Override
    public Void visitClass(CoolClass cclass) {
        for (var attr : cclass.attributes)
            visitAttribute(attr);
        for (var method : cclass.methods)
            visitMethod(method);
        return null;
    }

    @Override
    public Void visitAttribute(CoolAttribute attr) {
        VarSymbol symbol = new AttrSymbol(attr.name);

        if (attr.name.equals("self"))
            SymbolTable.error(attr.getContext(), attr.getNameToken(), String.format(
                "Class %s has attribute with illegal name self", classScope.classSymbol.name 
            ));

        if (!classScope.add(symbol, VarSymbol.namespace))
            SymbolTable.error(attr.getContext(), attr.getNameToken(), String.format(
                "Class %s redefines attribute %s", classScope.classSymbol.name, attr.name
            ));

        return null;
    }

    @Override
    public Void visitMethod(CoolMethod method) {
        MethodSymbol symbol = new MethodSymbol(method.name, classScope);

        if (!classScope.add(symbol, MethodSymbol.namespace))
            SymbolTable.error(method.getContext(), method.getNameToken(), String.format(
                "Class %s redefines method %s", classScope.classSymbol.name, method.name 
            ));

        if (method.type.equals("SELF_TYPE")) {
            symbol.setSelfType();
        } else {
            ClassSymbol typeSymbol = (ClassSymbol)SymbolTable.globals.lookup(method.type);
            if (typeSymbol == null)
                SymbolTable.error(method.getContext(), method.getTypeToken(), String.format(
                    "Class %s has method %s with undefined return type %s", 
                    classScope.classSymbol.name, method.name, method.type
                ));
            symbol.setType(typeSymbol);
        }

        method.setScope(classScope);
        method.setSymbol(symbol);

        methodScope = symbol.methodScope;
        formalIndex = 0;
        for (var formal : method.formalArgs) {
            visitFormalArg(formal);
            formalIndex += 1;
        }

        return null;
    }

    @Override
    public Void visitFormalArg(CoolFormalArg arg) {
        ArgSymbol symbol = new ArgSymbol(arg.name, formalIndex);

        if (arg.name.equals("self"))
            SymbolTable.error(arg.getContext(), arg.getNameToken(), String.format(
                "Method %s of class %s has formal parameter with illegal name self", 
                methodScope.methodSymbol.name, classScope.classSymbol.name
            ));

        if (arg.type.equals("SELF_TYPE")) {
            SymbolTable.error(arg.getContext(), arg.getTypeToken(), String.format(
                "Method %s of class %s has formal parameter %s with illegal type SELF_TYPE", 
                methodScope.methodSymbol.name, classScope.classSymbol.name, arg.name
            ));
        } else if (!methodScope.add(symbol, VarSymbol.namespace)) {
            SymbolTable.error(arg.getContext(), arg.getNameToken(), String.format(
                "Method %s of class %s redefines formal parameter %s", 
                methodScope.methodSymbol.name, classScope.classSymbol.name, arg.name
            ));
        } else {
            ClassSymbol typeSymbol = (ClassSymbol)SymbolTable.globals.lookup(arg.type);
            if (typeSymbol == null)
                SymbolTable.error(arg.getContext(), arg.getTypeToken(), String.format(
                    "Method %s of class %s has formal parameter %s with undefined type %s", 
                    methodScope.methodSymbol.name, classScope.classSymbol.name, arg.name, arg.type
                ));
            symbol.setType(typeSymbol);
        }

        arg.setSymbol(symbol);
        methodScope.methodSymbol.argsList.add(symbol);

        return null;
    }
}

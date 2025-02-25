package cool.structures;

import cool.parser.CoolASTBaseRecursiveVisitor;
import cool.parser.CoolASTNode.*;
import cool.structures.Symbol.*;
import cool.structures.Scope.*;

public class DefinitionPassVisitor extends CoolASTBaseRecursiveVisitor<Void> {
    Scope currentScope;

    public DefinitionPassVisitor() {
        this.currentScope = SymbolTable.globals;
    }

    @Override
    public Void visitProgram(CoolProgram program) {
        ClassSymbol mainClass = (ClassSymbol)SymbolTable.globals.lookup("Main");

        if (mainClass == null || mainClass.getScope().lookup("main", MethodSymbol.namespace) == null)
            SymbolTable.error("No method main in class Main");

        for (var cls : program.classes)
            visitClass(cls);

        return null;
    }

    @Override
    public Void visitClass(CoolClass cclass) {
        ClassSymbol classSymbol = (ClassSymbol)currentScope.lookup(cclass.name);

        currentScope = classSymbol.getScope();
        for (var attr : cclass.attributes)
            visitAttribute(attr);
        for (var method : cclass.methods)
            visitMethod(method);
        currentScope = SymbolTable.globals;
        return null;
    }

    @Override
    public Void visitAttribute(CoolAttribute attr) {
        ClassScope classScope = (ClassScope)currentScope;
        AttrSymbol symbol = (AttrSymbol)classScope.lookup(attr.name, VarSymbol.namespace);
        
        if (classScope.parent.lookup(attr.name, VarSymbol.namespace) != null)
            SymbolTable.error(attr.getContext(), attr.getNameToken(), String.format(
                "Class %s redefines inherited attribute %s", 
                classScope.classSymbol.name, attr.name
            ));

        if (symbol.getType(classScope) == null) {
            // add type if not redefinition
            if (attr.type.equals("SELF_TYPE")) {
                symbol.setSelfType();
            } else {
                ClassSymbol typeSymbol = (ClassSymbol)SymbolTable.globals.lookup(attr.type);
                if (typeSymbol == null)
                    SymbolTable.error(attr.getContext(), attr.getTypeToken(), String.format(
                        "Class %s has attribute %s with undefined type %s", 
                        classScope.classSymbol.name, attr.name, attr.type
                    ));
                symbol.setType(typeSymbol);
            }
        }
        attr.setScope(classScope);

        if (attr.init != null)
            visitExpr(attr.init);

        attr.setSymbol(symbol);
        return null;
    }

    @Override
    public Void visitMethod(CoolMethod method) {
        ClassScope classScope = (ClassScope)currentScope;
        MethodSymbol symbol = method.getSymbol();

        MethodSymbol parentMethod = 
            (MethodSymbol)classScope.parent.lookup(method.name, MethodSymbol.namespace);

        if (parentMethod != null) {
            if (parentMethod.argsList.size() != method.formalArgs.size())
                SymbolTable.error(method.getContext(), method.getNameToken(), String.format(
                    "Class %s overrides method %s with different number of formal parameters",
                    classScope.classSymbol.name, method.name
                ));

            for (int i = 0; i < method.formalArgs.size(); i++) {
                CoolFormalArg formal = method.formalArgs.get(i);
                ArgSymbol parentArg = parentMethod.argsList.get(i);
                ArgSymbol childArg = formal.getSymbol();
                if (!parentArg.getClassType().equals(childArg.getClassType()))
                    SymbolTable.error(method.getContext(), formal.getTypeToken(), String.format(
                        "Class %s overrides method %s but changes type of formal parameter %s from %s to %s",
                        classScope.classSymbol.name, method.name, childArg.name,
                        parentArg.getClassType().name, childArg.getClassType().name
                    ));
            }

            ClassSymbol parentReturnType = parentMethod.getClassType();
            ClassSymbol returnType = symbol.getClassType();
            if (parentReturnType != returnType)
                SymbolTable.error(method.getContext(), method.getTypeToken(), String.format(
                    "Class %s overrides method %s but changes return type from %s to %s",
                    classScope.classSymbol.name, method.name, 
                    parentReturnType != null ? parentReturnType.name : "SELF_TYPE", 
                    returnType.name != null ? returnType.name : "SELF_TYPE"
                ));
        }

        currentScope = symbol.methodScope;
        visitExpr(method.body);
        currentScope = currentScope.getParent();

        return null;
    }

    @Override
    public Void visitLet(CoolLet let) {
        Scope parentScope = currentScope;

        for (var binding : let.bindings) {
            visitLocalBinding(binding);  // this will change currentScope
        }

        visitExpr(let.body);
        currentScope = parentScope;
        return null;
    }

    // Not reentrant
    @Override
    public Void visitLocalBinding(CoolLocalBinding binding) {
        LocalSymbol symbol = new LocalSymbol(binding.name);

        if (binding.name.equals("self"))
            SymbolTable.error(binding.getContext(), binding.getNameToken(), 
                "Let variable has illegal name self");

        if (binding.type.equals("SELF_TYPE")) {
            symbol.setSelfType();
        } else {
            ClassSymbol typeSymbol = (ClassSymbol)SymbolTable.globals.lookup(binding.type);
            if (typeSymbol == null)
                SymbolTable.error(binding.getContext(), binding.getTypeToken(), 
                    String.format("Let variable %s has undefined type %s", 
                    binding.name, binding.type));
            symbol.setType(typeSymbol);
        }

        binding.setSymbol(symbol);
        binding.setScope(currentScope);

        if (binding.init != null)
            visitExpr(binding.init);

        currentScope = new SubClassScope((ClassScope)currentScope);
        currentScope.add(symbol, VarSymbol.namespace);
        return null;
    }

    @Override
    public Void visitCase(CoolCase ccase) {
        visitExpr(ccase.expr);

        for (var branch : ccase.branches) {
            visitCaseBranch(branch);
        }

        return null;
    }

    @Override
    public Void visitCaseBranch(CoolCaseBranch branch) {
        LocalSymbol symbol = new LocalSymbol(branch.name);

        if (branch.name.equals("self"))
            SymbolTable.error(branch.getContext(), branch.getNameToken(), 
                "Case variable has illegal name self");

        if (branch.type.equals("SELF_TYPE")) {
            SymbolTable.error(branch.getContext(), branch.getTypeToken(), 
                String.format("Case variable %s has illegal type SELF_TYPE", branch.name));
        } else {
            ClassSymbol typeSymbol = (ClassSymbol)SymbolTable.globals.lookup(branch.type);
            if (typeSymbol == null)
                SymbolTable.error(branch.getContext(), branch.getTypeToken(), 
                    String.format("Case variable %s has undefined type %s", 
                    branch.name, branch.type));
            symbol.setType(typeSymbol);
        }

        branch.setSymbol(symbol);
        
        currentScope = new SubClassScope((ClassScope)currentScope);
        currentScope.add(symbol, VarSymbol.namespace);
        visitExpr(branch.body);
        currentScope = currentScope.getParent();
        return null;
    }

    @Override
    public Void visitExplicitDispatch(CoolExplicitDispatch dispatch) {
        dispatch.setScope(currentScope);
        return super.visitExplicitDispatch(dispatch);
    }

    @Override
    public Void visitNewExpr(CoolNewExpr newexpr) {
        newexpr.setScope(currentScope);
        return null;
    }

    @Override
    public Void visitVar(CoolVar variable) {
        variable.setScope((ClassScope)currentScope);
        return null;
    }
}

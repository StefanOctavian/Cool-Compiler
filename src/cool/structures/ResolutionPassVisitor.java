package cool.structures;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import cool.lexer.CoolLexer;
import cool.parser.CoolASTBaseVisitor;
import cool.parser.CoolASTNode.*;
import cool.structures.Symbol.*;
import cool.structures.Scope.ClassScope;
import cool.structures.Scope.SubClassScope;

public class ResolutionPassVisitor extends CoolASTBaseVisitor<TypeSymbol> {
    private boolean isSubtype(TypeSymbol a, TypeSymbol b) {
        return a.isSubtypeOf(b);
    }

    private TypeSymbol leastUpperBound(TypeSymbol a, TypeSymbol b) {
        return a.leastUpperBound(b);
    }
    private TypeSymbol leastUpperBound(List<TypeSymbol> types) {
        if (types.isEmpty()) return SymbolTable.Object;
        TypeSymbol lub = types.get(0);
        for (var type : types) {
            lub = leastUpperBound(lub, type);
            if (lub == SymbolTable.Object) break;
        }
        return lub;
    }
    
    public final Set<String> allIntegers = new LinkedHashSet<>();
    public final Set<String> allStrings = new LinkedHashSet<>(
        List.of("", "Object", "IO", "String", "Int", "Bool"));

    @Override
    public TypeSymbol visitProgram(CoolProgram program) {
        for (var cls : program.classes)
            visitClass(cls);
        
        return null;
    }

    @Override
    public TypeSymbol visitClass(CoolClass cclass) {
        allStrings.add(cclass.name);
        allStrings.add(SymbolTable.getFileName(cclass.getContext()));

        for (var attr : cclass.attributes)
            visitAttribute(attr);
        for (var method : cclass.methods)
            visitMethod(method);

        return null;
    }

    @Override
    public TypeSymbol visitAttribute(CoolAttribute attr) {
        if (attr.init == null)
            return null;

        TypeSymbol initType = visitExpr(attr.init);
        TypeSymbol attrType = attr.getSymbol().getType((ClassScope)attr.getScope());

        if (initType != null && !isSubtype(initType, attrType)) {
            SymbolTable.error(attr.getContext(), attr.init.getToken(), String.format(
                "Type %s of initialization expression of attribute %s is incompatible " +
                "with declared type %s", initType.getName(), attr.name, attrType.getName()
            ));
        }
        return null;
    }

    @Override
    public TypeSymbol visitMethod(CoolMethod method) {
        TypeSymbol bodyType = visitExpr(method.body);
        TypeSymbol returnType = method.getSymbol().getType((ClassScope)method.getScope());
        if (bodyType != null && !isSubtype(bodyType, returnType)) {
            SymbolTable.error(method.getContext(), method.body.getToken(), String.format(
                "Type %s of the body of method %s is incompatible with declared return type %s",
                bodyType.getName(), method.name, returnType.getName()
            ));
        }
        return null;
    }

    @Override
    public TypeSymbol visitExpr(CoolExpr expr) {
        return expr.accept(this);
    }

    @Override
    public TypeSymbol visitAssignment(CoolAssignment assignment) {
        if (assignment.variable.name.equals("self"))
            SymbolTable.error(assignment.getContext(), assignment.variable.getToken(), 
                "Cannot assign to self");

        TypeSymbol declType = visitVar(assignment.variable);
        TypeSymbol valType = visitExpr(assignment.value);

        if (valType != null && !isSubtype(valType, declType)) {
            SymbolTable.error(assignment.getContext(), assignment.value.getToken(), String.format(
                "Type %s of assigned expression is incompatible with declared type %s of identifier %s",
                valType.getName(), declType.getName(), assignment.variable.name
            ));
        }
        return valType;
    }

    @Override
    public TypeSymbol visitUnaryOperation(CoolUnaryOperation operation) {
        TypeSymbol operandType = visitExpr(operation.operand);
        if (operation.getToken().getType() == CoolLexer.NOT) {
            if (operandType != null && operandType != SymbolTable.Bool) {
                SymbolTable.error(operation.getContext(), operation.operand.getToken(), String.format(
                    "Operand of not has type %s instead of Bool", operandType.getName()
                ));
            }
            return SymbolTable.Bool;
        }
        if (operation.getToken().getType() == CoolLexer.NEG) {
            if (operandType != null && operandType != SymbolTable.Int) {
                SymbolTable.error(operation.getContext(), operation.operand.getToken(), String.format(
                    "Operand of %s has type %s instead of Int",
                    operation.op, operandType.getName()
                ));
            }
            return SymbolTable.Int;
        }
        // the other case is "isvoid" operator
        return SymbolTable.Bool;
    }

    @Override
    public TypeSymbol visitBinaryOperation(CoolBinaryOperation operation) {
        TypeSymbol lhsType = visitExpr(operation.lhs);
        TypeSymbol rhsType = visitExpr(operation.rhs);

        Set<TypeSymbol> primTypes = Set.of(SymbolTable.Int, SymbolTable.Bool, SymbolTable.String);
        if (operation.getOpToken().getType() == CoolLexer.EQ) {
            if (lhsType == null || rhsType == null)
                return SymbolTable.Bool;

            if ((primTypes.contains(lhsType) || primTypes.contains(rhsType)) && lhsType != rhsType)
                SymbolTable.error(operation.getContext(), operation.getOpToken(), String.format(
                    "Cannot compare %s with %s", lhsType.getName(), rhsType.getName()
                )); 
            return SymbolTable.Bool;
        }
        BiConsumer<TypeSymbol, CoolExpr> errorIfNotInt = (TypeSymbol type, CoolExpr operand) -> {
            if (type != null && type != SymbolTable.Int)
                SymbolTable.error(operand.getContext(), operand.getToken(), String.format(
                    "Operand of %s has type %s instead of Int",
                    operation.op, type.getName()
                ));
        };
        errorIfNotInt.accept(lhsType, operation.lhs);
        errorIfNotInt.accept(rhsType, operation.rhs);
        if (operation.getOpToken().getType() == CoolLexer.LT || 
            operation.getOpToken().getType() == CoolLexer.LEQ) {
            return SymbolTable.Bool;
        }
        return SymbolTable.Int;
    }

    @Override
    public TypeSymbol visitBlock(CoolBlock block) {
        TypeSymbol lastType = null;

        for (var expr : block.exprs)
            lastType = visitExpr(expr);

        return lastType;
    }

    @Override
    public TypeSymbol visitLet(CoolLet let) {
        for (var binding : let.bindings)
            visitLocalBinding(binding);

        return visitExpr(let.body);
    }

    @Override
    public TypeSymbol visitLocalBinding(CoolLocalBinding binding) {
        if (binding.init == null)
            return null;

        TypeSymbol initType = visitExpr(binding.init);
        TypeSymbol bindingType = binding.getSymbol().getType((ClassScope)binding.getScope());
        if (initType != null && !isSubtype(initType, bindingType)) {
            SymbolTable.error(binding.getContext(), binding.init.getToken(), String.format(
                "Type %s of initialization expression of identifier %s is incompatible " +
                "with declared type %s", initType.getName(), binding.name, bindingType.getName()
            ));
        }
        return null;
    }

    @Override
    public TypeSymbol visitCase(CoolCase ccase) {
        visitExpr(ccase.expr);
        List<TypeSymbol> branchTypes = new ArrayList<>();
        
        for (var branch : ccase.branches) {
            TypeSymbol branchType = visitCaseBranch(branch);
            if (branchType != null) branchTypes.add(branchType);
        }

        return leastUpperBound(branchTypes);
    }

    @Override
    public TypeSymbol visitCaseBranch(CoolCaseBranch branch) {
        return visitExpr(branch.body);
    }

    @Override
    public TypeSymbol visitIf(CoolIf cif) {
        TypeSymbol condType = visitExpr(cif.cond);
        TypeSymbol thenType = visitExpr(cif.thenBranch);
        TypeSymbol elseType = visitExpr(cif.elseBranch);

        if (condType != null && condType != SymbolTable.Bool)
            SymbolTable.error(cif.getContext(), cif.cond.getToken(), String.format(
                "If condition has type %s instead of Bool", condType.name
            ));

        return leastUpperBound(thenType, elseType);
    }

    @Override
    public TypeSymbol visitWhile(CoolWhile cwhile) {
        TypeSymbol condType = visitExpr(cwhile.cond);
        if (condType != null && condType != SymbolTable.Bool)
            SymbolTable.error(cwhile.getContext(), cwhile.cond.getToken(), String.format(
                "While condition has type %s instead of Bool", condType.name
            ));
        visitExpr(cwhile.body);
        return SymbolTable.Object;
    }

    @Override
    public TypeSymbol visitNewExpr(CoolNewExpr newexpr) {
        TypeSymbol type;
        
        if (newexpr.type.equals("SELF_TYPE"))
            type = ((SubClassScope)newexpr.getScope()).classSymbol.selfType;
        else
            type = (TypeSymbol)SymbolTable.globals.lookup(newexpr.type);

        if (type == null)
            SymbolTable.error(newexpr.getContext(), newexpr.getTypeToken(), 
                "new is used with undefined type " + newexpr.type);

        return type;
    }

    @Override
    public TypeSymbol visitExplicitDispatch(CoolExplicitDispatch dispatch) {
        MethodSymbol methodSymbol = null;
        TypeSymbol objectType = null;  // type of the object on which the method is called
        ClassScope classScope = null;  // scope of the (static/dynamic) class of the object
        ClassSymbol classSymbol = null;  // (static/dynamic) class symbol of the object
        
        if (dispatch.object == null) {
            // implicit (dynamic) dispatch
            objectType = ((ClassScope)dispatch.getScope()).classSymbol.selfType;
        } else {
            objectType = visitExpr(dispatch.object);
            if (objectType == null) 
                return null;
        }

        if (dispatch.staticClass == null) {
            // dynamic dispatch
            if (objectType instanceof SelfTypeSymbol)
                classScope = ((SelfTypeSymbol)objectType).classSymbol.getScope();
            else
                classScope = ((ClassSymbol)objectType).getScope();
            classSymbol = classScope.getSymbol();
        } else {
            // static dispatch
            if (dispatch.staticClass.equals("SELF_TYPE")) {
                SymbolTable.error(dispatch.getContext(), dispatch.getStaticClassToken(),
                "Type of static dispatch cannot be SELF_TYPE");
                return null;
            }
            classSymbol = (ClassSymbol)SymbolTable.globals.lookup(dispatch.staticClass);
            if (classSymbol == null) {
                SymbolTable.error(dispatch.getContext(), dispatch.getStaticClassToken(), 
                    String.format("Type %s of static dispatch is undefined",
                        dispatch.staticClass));
                return null;
            }
            if (!isSubtype(objectType, classSymbol)) {
                SymbolTable.error(dispatch.getContext(), dispatch.getStaticClassToken(), String.format(
                    "Type %s of static dispatch is not a superclass of type %s",
                    classSymbol.name, objectType.name
                ));
                return null;
            }
            classScope = classSymbol.getScope();
        }

        methodSymbol = (MethodSymbol)classScope.lookup(dispatch.method, MethodSymbol.namespace);

        if (methodSymbol == null) {
            SymbolTable.error(dispatch.getContext(), dispatch.getMethodToken(), 
                String.format("Undefined method %s in class %s",
                    dispatch.method, classSymbol.getName()));
            return null;
        }

        if (dispatch.params.size() != methodSymbol.argsList.size()) {
            SymbolTable.error(dispatch.getContext(), dispatch.getMethodToken(), String.format(
                "Method %s of class %s is applied to wrong number of arguments" ,
                dispatch.method, classSymbol.name
            ));
        }

        dispatch.setSymbol(methodSymbol);

        for (int i = 0; i < dispatch.params.size(); i++) {
            CoolExpr param = dispatch.params.get(i);
            VarSymbol arg = methodSymbol.argsList.get(i);
            TypeSymbol paramType = visitExpr(param);
            ClassSymbol argType = arg.getClassType();
            if (paramType != null && !isSubtype(paramType, argType)) {
                SymbolTable.error(param.getContext(), param.getToken(), String.format(
                    "In call to method %s of class %s, actual type %s of formal " +
                    "parameter %s is incompatible with declared type %s",
                    dispatch.method, classSymbol.name, paramType.getName(), arg.name, argType.name
                ));
            }
        }

        ClassSymbol returnType = methodSymbol.getClassType();  // returns null if SELF_TYPE
        return (returnType == null) ? objectType : returnType;
    }

    @Override
    public TypeSymbol visitImplicitDispatch(CoolImplicitDispatch dispatch) {
        return visitExplicitDispatch(dispatch);
    }

    @Override
    public TypeSymbol visitVar(CoolVar variable) {
        ClassScope classScope = (ClassScope)variable.getScope();

        IdSymbol symbol = variable.name.equals("self") ?
            classScope.classSymbol.self : 
            (IdSymbol)classScope.lookup(variable.name, VarSymbol.namespace);

        if (symbol == null) {
            SymbolTable.error(variable.getContext(), variable.getToken(), 
                "Undefined identifier " + variable.name);
            return null;
        }

        variable.setSymbol(symbol);
        return symbol.getType(classScope);
    }

    @Override
    public TypeSymbol visitLiteral(CoolLiteral lit) {
        if (lit.getToken().getType() == CoolLexer.INTEGER) {
            allIntegers.add(lit.getToken().getText());
            return SymbolTable.Int;
        }

        if (lit.getToken().getType() == CoolLexer.STRING) {
            allStrings.add(lit.getToken().getText());
            return SymbolTable.String;
        }

        if (lit.getToken().getType() == CoolLexer.TRUE || 
            lit.getToken().getType() == CoolLexer.FALSE)
            return SymbolTable.Bool;

        return null;
    }
}

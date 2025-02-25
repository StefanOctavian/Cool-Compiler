package cool.parser;

import cool.parser.CoolASTNode.*;

// A base implementation for the AST visitor that visits all nodes recursively (recursion is opt-out)
public class CoolASTBaseRecursiveVisitor<T> implements CoolASTVisitor<T> {
    public T visit(CoolASTNode node) {
        return node.accept(this);
    };

    @Override
    public T visitProgram(CoolProgram program) {
        for (CoolClass classNode : program.classes)
            visitClass(classNode);

        return null;
    }

    @Override
    public T visitClass(CoolClass cclass) {
        for (CoolAttribute attr : cclass.attributes)
            visitAttribute(attr);
        for (CoolMethod method : cclass.methods)
            visitMethod(method);

        return null;
    }

    @Override
    public T visitAttribute(CoolAttribute attr) {
        if (attr.init != null)
            visitExpr(attr.init);

        return null;
    }

    @Override
    public T visitMethod(CoolMethod method) {
        for (CoolFormalArg formal : method.formalArgs)
            visitFormalArg(formal);

        return visitExpr(method.body);
    }

    @Override
    public T visitFormalArg(CoolFormalArg arg) {
        return null;
    }

    @Override
    public T visitExpr(CoolExpr expr) {
        return expr.accept(this);
    }

    @Override
    public T visitAssignment(CoolAssignment assignment) {
        visitVar(assignment.variable);
        return visitExpr(assignment.value);
    }

    @Override
    public T visitUnaryOperation(CoolUnaryOperation operation) {
        return visitExpr(operation.operand);
    }

    @Override
    public T visitBinaryOperation(CoolBinaryOperation operation) {
        visitExpr(operation.lhs);
        visitExpr(operation.rhs);
        return null;
    }

    @Override
    public T visitLet(CoolLet let) {
        for (CoolLocalBinding binding : let.bindings)
            visitLocalBinding(binding);

        return visitExpr(let.body);
    }

    @Override
    public T visitLocalBinding(CoolLocalBinding binding) {
        if (binding.init != null)
            visitExpr(binding.init);

        return null;
    }

    @Override
    public T visitNewExpr(CoolNewExpr newexpr) {
        return null;
    }

    @Override
    public T visitExplicitDispatch(CoolExplicitDispatch dispatch) {
        if (dispatch.object != null)
            visitExpr(dispatch.object);

        for (CoolExpr arg : dispatch.params)
            visitExpr(arg);

        return null;
    }

    @Override
    public T visitImplicitDispatch(CoolImplicitDispatch dispatch) {
        return visitExplicitDispatch(dispatch);
    }

    @Override
    public T visitCase(CoolCase ccase) {
        visitExpr(ccase.expr);

        for (CoolCaseBranch branch : ccase.branches)
            visitCaseBranch(branch);

        return null;
    }

    @Override
    public T visitCaseBranch(CoolCaseBranch branch) {
        return visitExpr(branch.body);
    }

    @Override
    public T visitIf(CoolIf cif) {
        visitExpr(cif.cond);
        visitExpr(cif.thenBranch);
        visitExpr(cif.elseBranch);
        return null;
    }

    @Override
    public T visitWhile(CoolWhile cwhile) {
        visitExpr(cwhile.cond);
        visitExpr(cwhile.body);
        return null;
    }

    @Override
    public T visitBlock(CoolBlock block) {
        for (CoolExpr expr : block.exprs)
            visitExpr(expr);

        return null;
    }

    @Override
    public T visitVar(CoolVar variable) {
        return null;
    }

    @Override
    public T visitLiteral(CoolLiteral lit) {
        return null;
    }
}